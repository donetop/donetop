package com.donetop.main.api.nhn.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class TradeRegisterRequest {

	@NotEmpty(message = "상점 ID 값은 필수입니다.")
	private String site_cd;

	@NotEmpty(message = "주문번호 값은 필수입니다.")
	private String ordr_idxx;

	@NotEmpty(message = "주문금액 값은 필수입니다.")
	private String good_mny;

	@NotEmpty(message = "결제수단 값은 필수입니다.")
	private String pay_method;

	@NotEmpty(message = "상품명 값은 필수입니다.")
	private String good_name;

	@JsonProperty("Ret_URL")
	@NotEmpty(message = "응답 주소 값은 필수입니다.")
	private String ret_url;

	@NotEmpty(message = "에스크로 사용여부 값은 필수입니다.")
	private String escw_used;

	@NotEmpty(message = "user agent 값은 필수입니다.")
	private String user_agent;

	public JSONObject toJsonObject() throws Exception {
		return new JSONObject()
			.put("site_cd", this.site_cd)
			.put("ordr_idxx", this.ordr_idxx)
			.put("good_mny", this.good_mny)
			.put("pay_method", this.pay_method)
			.put("good_name", this.good_name)
			.put("Ret_URL", this.ret_url)
			.put("escw_used", this.escw_used)
			.put("user_agent", this.user_agent);
	}

}
