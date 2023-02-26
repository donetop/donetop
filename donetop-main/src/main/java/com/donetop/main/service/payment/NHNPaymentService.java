package com.donetop.main.service.payment;

import com.donetop.domain.entity.draft.Draft;
import com.donetop.domain.entity.payment.PaymentHistory;
import com.donetop.domain.entity.payment.PaymentInfo;
import com.donetop.enums.payment.PGType;
import com.donetop.enums.payment.Detail.NHNPaymentDetail;
import com.donetop.main.api.payment.request.NHNPaymentRequest;
import com.donetop.main.properties.ApplicationProperties;
import com.donetop.main.properties.ApplicationProperties.Payment.NHN;
import com.donetop.repository.draft.DraftRepository;
import com.donetop.repository.payment.PaymentHistoryRepository;
import com.donetop.repository.payment.PaymentInfoRepository;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.joining;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Service
@Transactional
public class NHNPaymentService {

	private static final String kcp_cert_info;

	private final NHN nhn;

	private final DraftRepository draftRepository;

	private final PaymentInfoRepository paymentInfoRepository;
	private final PaymentHistoryRepository paymentHistoryRepository;

	public NHNPaymentService(final ApplicationProperties applicationProperties,
							 final DraftRepository draftRepository,
							 final PaymentInfoRepository paymentInfoRepository,
							 final PaymentHistoryRepository paymentHistoryRepository) {
		this.nhn = applicationProperties.getPayment().getNhn();
		this.draftRepository = draftRepository;
		this.paymentInfoRepository = paymentInfoRepository;
		this.paymentHistoryRepository = paymentHistoryRepository;
	}

	static {
		try {
			final Path path = Paths.get(new ClassPathResource("nhn/splCert.pem").getURI());
			kcp_cert_info = String.join("", Files.readAllLines(path));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public NHNPaymentDetail payment(final NHNPaymentRequest paymentRequest) throws Exception {
		final JSONObject json = new JSONObject();
		json.put("tran_cd", paymentRequest.getTran_cd());
		json.put("site_cd", paymentRequest.getSite_cd());
		json.put("kcp_cert_info", kcp_cert_info);
		json.put("enc_data", paymentRequest.getEnc_data());
		json.put("enc_info", paymentRequest.getEnc_info());
		json.put("ordr_mony", paymentRequest.getGood_mny());

		final URL url = new URL(nhn.getTargetURL());
		final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod(HttpMethod.POST.name());
		conn.setRequestProperty("Content-Type", APPLICATION_JSON_VALUE);
		conn.setRequestProperty("Accept-Charset", UTF_8.toString());

		final OutputStream os = conn.getOutputStream();
		os.write(json.toString().getBytes(UTF_8));
		os.flush();

		final String rawData = new BufferedReader(new InputStreamReader(conn.getInputStream(), UTF_8)).lines().collect(joining());
		conn.disconnect();

		return savePaymentHistory(rawData);
	}

	private NHNPaymentDetail savePaymentHistory(final String rawData) {
		final NHNPaymentDetail nhnPaymentDetail = (NHNPaymentDetail) PGType.NHN.detail(rawData);
		final Long draftId = Long.valueOf(nhnPaymentDetail.getOrder_no().split("-")[1]);
		final Draft draft = draftRepository.findById(draftId)
			.orElseThrow(() -> new IllegalStateException("결제 정보 저장을 위한 유효한 시안이 없습니다."));
		final PaymentInfo paymentInfo = draft.getOrNewPaymentInfo();
		if (paymentInfo.isNew()) {
			paymentInfoRepository.save(paymentInfo);
			draft.addPaymentInfo(paymentInfo);
		}
		final PaymentHistory paymentHistory = new PaymentHistory().toBuilder()
			.pgType(PGType.NHN)
			.rawData(rawData)
			.paymentInfo(paymentInfo).build();
		paymentHistoryRepository.save(paymentHistory);
		return nhnPaymentDetail;
	}

}
