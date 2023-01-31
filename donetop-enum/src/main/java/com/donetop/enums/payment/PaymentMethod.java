package com.donetop.enums.payment;

public enum PaymentMethod {
	CASH,
	CREDIT_CARD,
	;

	public static PaymentMethod of(final String value) {
		for (final PaymentMethod paymentMethod : values()) {
			if (paymentMethod.toString().equals(value)) return paymentMethod;
		}
		throw new IllegalArgumentException("There's no valid enum value for " + value);
	}
}
