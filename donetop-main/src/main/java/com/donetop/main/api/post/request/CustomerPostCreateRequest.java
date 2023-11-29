package com.donetop.main.api.post.request;

import com.donetop.domain.entity.post.CustomerPost;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class CustomerPostCreateRequest {

	@NotEmpty(message = "고객명을 입력해주세요.")
	private String customerName;

	@NotEmpty(message = "이메일을 입력해주세요.")
	private String email;

	@NotEmpty(message = "제목을 입력해주세요.")
	private String title;

	@NotEmpty(message = "내용을 입력해주세요.")
	private String content;

	public CustomerPost toEntity() {
		return new CustomerPost().toBuilder()
			.customerName(this.customerName)
			.email(this.email)
			.title(this.title)
			.content(this.content)
			.build();
	}

}
