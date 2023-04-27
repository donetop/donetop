package com.donetop.enums.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NameOfEnumValidator implements ConstraintValidator<NameOfEnum, String> {

	private List<String> acceptedNames;

	@Override
	public void initialize(final NameOfEnum annotation) {
		acceptedNames = Stream.of(annotation.enumClass().getEnumConstants())
			.map(Enum::name)
			.collect(Collectors.toList());
	}

	@Override
	public boolean isValid(final String name, final ConstraintValidatorContext context) {
		return acceptedNames.contains(name);
	}
}
