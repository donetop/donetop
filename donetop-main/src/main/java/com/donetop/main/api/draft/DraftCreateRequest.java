package com.donetop.main.api.draft;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class DraftCreateRequest {

	@NotEmpty(message = "고객명을 입력해주세요.")
	private String customerName;



}
