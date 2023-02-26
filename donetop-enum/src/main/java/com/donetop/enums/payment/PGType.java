package com.donetop.enums.payment;

import com.donetop.enums.payment.Detail.NHNPaymentDetail;
import lombok.RequiredArgsConstructor;

import java.util.function.Function;

@RequiredArgsConstructor
public enum PGType {

	NHN(NHNPaymentDetail::from),
	;

	private final Function<String, Detail> function;

	public Detail detail(final String rawData) {
		return function.apply(rawData);
	}

}
