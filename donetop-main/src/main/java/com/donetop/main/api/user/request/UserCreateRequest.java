package com.donetop.main.api.user.request;

import com.donetop.domain.entity.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class UserCreateRequest {

	@NotEmpty(message = "이메일을 입력해주세요.")
	private String email;

	@NotEmpty(message = "이름을 입력해주세요.")
	private String name;

	@NotEmpty(message = "비밀번호를 입력해주세요.")
	private String password;

	public User toEntity() {
		return User.builder()
			.email(this.email)
			.name(this.name)
			.password(this.password)
			.build();
	}
}
