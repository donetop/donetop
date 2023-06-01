package com.donetop.enums.draft;

import com.donetop.enums.common.EnumDTO;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public enum PaymentMethod {
	CASH("현금결제"),
	CREDIT_CARD("카드결제"),
	;

	private final String value;

	public String value() {
		return this.value;
	}

	public EnumDTO toDTO() {
		return new EnumDTO(this.name(), this.value);
	}

	public static PaymentMethod of(final String value) {
		for (final PaymentMethod paymentMethod : values()) {
			if (paymentMethod.value.equals(value)) return paymentMethod;
		}
		throw new IllegalArgumentException("There's no valid enum value for " + value);
	}

	public static List<EnumDTO> dtoList() {
		return Stream.of(values()).map(PaymentMethod::toDTO).collect(Collectors.toList());
	}
}
