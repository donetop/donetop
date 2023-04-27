package com.donetop.main.api.draft.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;

@Getter @Setter
public class DraftCopyRequest {

	@Min(value = 1L, message = "시안 아이디가 유효하지 않습니다.")
	private long id;

}
