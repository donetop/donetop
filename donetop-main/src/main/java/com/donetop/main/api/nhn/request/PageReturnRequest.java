package com.donetop.main.api.nhn.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class PageReturnRequest extends PaymentRequest {

	@NotEmpty(message = "param_opt_1 값은 필수입니다.")
	private String param_opt_1;

}
