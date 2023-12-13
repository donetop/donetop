package com.donetop.main.api.post.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class CustomerPostCommentCreateRequest {

	@NotEmpty(message = "내용을 입력해주세요.")
	private String content;

	@Min(value = 1L, message = "유효하지 않은 고객 게시물 아이디입니다.")
	private long customerPostId;

}
