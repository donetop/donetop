package com.donetop.enums.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ValueOfEnumValidator implements ConstraintValidator<ValueOfEnum, String> {

	private List<String> acceptedValues;

	@Override
	public void initialize(final ValueOfEnum annotation) {
		acceptedValues = Stream.of(annotation.enumClass().getEnumConstants())
			.map(Enum::name)
			.collect(Collectors.toList());
	}

	@Override
	public boolean isValid(final String value, final ConstraintValidatorContext context) {
		return acceptedValues.contains(value);
	}
}
