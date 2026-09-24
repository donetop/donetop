package com.donetop.main.api.sms.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter @Setter
public class VerificationCodeVerifyRequest {

	@NotBlank(message = "전화번호를 입력해주세요.")
	@Pattern(
		regexp = "^01[016789]-\\d{3,4}-\\d{4}$",
		message = "올바른 휴대폰 번호 형식이 아닙니다."
	)
	private String phoneNumber;

	@NotBlank(message = "코드를 입력해주세요.")
	private String code;

}
