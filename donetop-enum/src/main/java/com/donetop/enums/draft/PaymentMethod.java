package com.donetop.enums.draft;

import com.donetop.enums.common.EnumDTO;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public enum PaymentMethod {
	CASH("현금결제"),
	CREDIT_CARD("신용카드"),
	;

	private final String value;

	public String value() {
		return this.value;
	}

	public EnumDTO toDTO() {
		return new EnumDTO(this.name(), this.value);
	}

	public static PaymentMethod of(final String name) {
		for (final PaymentMethod paymentMethod : values()) {
			if (paymentMethod.name().equals(name)) return paymentMethod;
		}
		throw new IllegalArgumentException("There's no valid enum name for " + name);
	}

	public static List<EnumDTO> dtoList() {
		return Stream.of(values()).map(PaymentMethod::toDTO).collect(Collectors.toList());
	}
}
