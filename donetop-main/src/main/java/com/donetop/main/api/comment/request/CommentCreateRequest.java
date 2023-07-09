package com.donetop.main.api.comment.request;

import com.donetop.common.api.MultipartFilesRequest;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class CommentCreateRequest extends MultipartFilesRequest {

	@NotEmpty(message = "내용을 입력해주세요.")
	private String content;

	@Min(value = 1L, message = "유효하지 않은 시안 아이디입니다.")
	private long draftId;

}
