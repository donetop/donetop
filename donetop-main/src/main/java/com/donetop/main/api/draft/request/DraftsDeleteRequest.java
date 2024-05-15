package com.donetop.main.api.draft.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class DraftsDeleteRequest {

	@NotEmpty(message = "삭제할 시안 항목 아이디를 입력해주세요.")
	private List<Long> draftIds = new ArrayList<>();

}
