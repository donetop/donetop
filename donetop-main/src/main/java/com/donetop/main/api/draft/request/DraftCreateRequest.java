package com.donetop.main.api.draft.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class DraftCreateRequest {

	@NotEmpty(message = "고객명을 입력해주세요.")
	private String customerName;

	private String address;

	@Min(value = 1000L, message = "최소 금액은 1000원입니다.")
	private long price;

	private String memo;
}
