package com.donetop.enums.payment;

import com.donetop.enums.payment.Detail.NHNPaidDetail;

import java.util.Map;
import java.util.function.Function;

public enum PGType {

	NHN(NHNPaidDetail::from, Detail.NHNCancelDetail::from),
	;

	private final Map<PaymentStatus, Function<String, Detail>> functionMap;

	PGType(final Function<String, Detail> paidFunction, final Function<String, Detail> canceledFunction) {
		this.functionMap = Map.ofEntries(
			Map.entry(PaymentStatus.PAID, paidFunction),
			Map.entry(PaymentStatus.CANCELED, canceledFunction)
		);
	}

	public Detail detail(final PaymentStatus paymentStatus, final String rawData) {
		return functionMap.get(paymentStatus).apply(rawData);
	}

	public static PGType of(final String name) {
		for (final PGType pgType : values()) {
			if (pgType.name().equals(name)) return pgType;
		}
		throw new IllegalArgumentException("There's no valid enum name for " + name);
	}

}
