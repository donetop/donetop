package com.donetop.main.api.authentication.form;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.util.StringUtils;

@Getter @Setter
public class FormLoginRequest {

	private String username;
	private String password;

	public boolean isEmpty() {
		return !StringUtils.hasLength(username) || !StringUtils.hasLength(password);
	}

	public UsernamePasswordAuthenticationToken toToken() {
		return new UsernamePasswordAuthenticationToken(this.username, this.password);
	}

}
