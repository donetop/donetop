package com.donetop.enums.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = NameOfEnumValidator.class)
public @interface NameOfEnum {

	Class<? extends Enum<?>> enumClass();

	String message() default "{enumClass}에 정의된 이름을 사용해주세요.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
