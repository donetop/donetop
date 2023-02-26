package com.donetop.enums.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

public interface Detail {

	ObjectMapper objectMapper = new ObjectMapper()
		.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

	@Getter @Setter
	class NHNPaymentDetail implements Detail {

		private String res_cd;

		private String res_msg;

		private String tno;

		private String order_no;

		private long amount;

		private String card_name;

		private String card_no;

		private String pg_txid;

		public static Detail from(final String rawData) {
			try {
				return objectMapper.readValue(rawData, NHNPaymentDetail.class);
			} catch (JsonProcessingException e) {
				throw new RuntimeException(e);
			}
		}

	}

}
