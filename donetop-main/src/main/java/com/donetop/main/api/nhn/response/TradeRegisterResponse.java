package com.donetop.main.api.nhn.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TradeRegisterResponse {

	private static final ObjectMapper objectMapper = new ObjectMapper()
		.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

	@JsonProperty("Message")
	private String message;

	private String approvalKey;

	@JsonProperty("PayUrl")
	private String payUrl;

	private String hashData;

	private String traceNo;

	private String paymentMethod;

	private String request_URI;

	@JsonProperty("Code")
	private String code;

	public static TradeRegisterResponse of(final String rawData) throws Exception {
		return objectMapper.readValue(rawData, TradeRegisterResponse.class);
	}

}
