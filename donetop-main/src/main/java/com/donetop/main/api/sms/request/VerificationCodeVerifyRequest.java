package com.donetop.main.api.sms.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static com.donetop.common.api.Message.*;

@Getter @Setter
public class VerificationCodeVerifyRequest {

	@NotBlank(message = SMS_NO_PHONE_NUMBER)
	@Pattern(
		regexp = "^01[016789]-\\d{3,4}-\\d{4}$",
		message = SMS_INVALID_PHONE_NUMBER_FORMAT
	)
	private String phoneNumber;

	@NotBlank(message = SMS_NO_CODE)
	private String code;

}
