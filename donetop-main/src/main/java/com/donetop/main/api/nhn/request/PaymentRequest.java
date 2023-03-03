package com.donetop.main.api.nhn.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class PaymentRequest {

	@NotEmpty(message = "KCP 발급 사이트(상점)코드 값은 필수입니다.")
	private String site_cd;

	@NotEmpty(message = "결제창 인증결과 암호화 정보 값은 필수입니다.")
	private String enc_data;

	@NotEmpty(message = "결제창 인증결과 암호화 정보 값은 필수입니다.")
	private String enc_info;

	@NotEmpty(message = "결제요청타입 값은 필수입니다.")
	private String tran_cd;

	@Min(value = 1000L, message = "허용된 최소 결제요청 금액은 1000원입니다.")
	private long good_mny;

	public JSONObject toJsonObject() {
		try {
			return new JSONObject()
				.put("tran_cd", this.tran_cd)
				.put("site_cd", this.site_cd)
				.put("enc_data", this.enc_data)
				.put("enc_info", this.enc_info)
				.put("ordr_mony", this.good_mny);
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}
}
