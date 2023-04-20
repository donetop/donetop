package com.donetop.common.nhn;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JceOpenSSLPKCS8DecryptorProviderBuilder;
import org.bouncycastle.operator.InputDecryptorProvider;
import org.bouncycastle.pkcs.PKCS8EncryptedPrivateKeyInfo;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotEmpty;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.PrivateKey;
import java.security.Signature;
import java.util.Base64;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.joining;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@Getter
@Setter
@Validated
@Configuration
@ConfigurationProperties(prefix = "nhn")
public class NHN {

	@NotEmpty
	private String siteCD;

	@NotEmpty
	private String paymentURL;

	@NotEmpty
	private String tradeRegisterURL;

	@NotEmpty
	private String cancelURL;

	@NotEmpty
	private String certPath;

	@NotEmpty
	private String privateKeyPath;

	@NotEmpty
	private String privateKeyPassword;

	private String certData;

	private String privateKeyData;

	@PostConstruct
	private void postConstruct() {
		this.certData = loadCertData();
		this.privateKeyData = loadPrivateKeyData();
	}

	private String loadCertData() {
		try {
			log.info("NHN cert file path: {}", certPath);
			final Path path = Paths.get(new ClassPathResource(certPath).getURI());
			return String.join("", Files.readAllLines(path));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private String loadPrivateKeyData() {
		try {
			log.info("NHN private key file path: {}", privateKeyPath);
			final Path path = Paths.get(new ClassPathResource(privateKeyPath).getURI());
			return Files.readAllLines(path)
				.stream()
				.filter(line -> !line.startsWith("-----BEGIN") && !line.startsWith("-----END"))
				.collect(Collectors.joining());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String doRequest(final String urlString, final JSONObject jsonObject) {
		try {
			final URL url = new URL(urlString);
			final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod(HttpMethod.POST.name());
			conn.setRequestProperty("Content-Type", APPLICATION_JSON_VALUE);
			conn.setRequestProperty("Accept-Charset", UTF_8.toString());

			final OutputStream os = conn.getOutputStream();
			os.write(jsonObject.toString().getBytes(UTF_8));
			os.flush();

			final String rawData = new BufferedReader(new InputStreamReader(conn.getInputStream(), UTF_8)).lines().collect(joining());
			conn.disconnect();

			return rawData;
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	public PrivateKey loadSplMctPrivateKeyPKCS8() {
		try {

			final byte[] btArrPriKey = Base64.getDecoder().decode(privateKeyData);

			final ASN1Sequence derSeq = ASN1Sequence.getInstance(btArrPriKey);
			final PKCS8EncryptedPrivateKeyInfo encPKCS8PriKeyInfo = new PKCS8EncryptedPrivateKeyInfo(org.bouncycastle.asn1.pkcs.EncryptedPrivateKeyInfo.getInstance(derSeq));
			final JcaPEMKeyConverter pemKeyConverter = new JcaPEMKeyConverter();
			final InputDecryptorProvider decProvider = new JceOpenSSLPKCS8DecryptorProviderBuilder().build(privateKeyPassword.toCharArray());
			final PrivateKeyInfo priKeyInfo = encPKCS8PriKeyInfo.decryptPrivateKeyInfo(decProvider);

			return pemKeyConverter.getPrivateKey(priKeyInfo);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String makeSignatureData(final PrivateKey priKey, final String targetData) {
		try {
			byte[] btArrTargetData = targetData.getBytes(StandardCharsets.UTF_8);

			final Signature sign = Signature.getInstance("SHA256WithRSA");
			sign.initSign(priKey);
			sign.update(btArrTargetData);

			return Base64.getEncoder().encodeToString(sign.sign());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
