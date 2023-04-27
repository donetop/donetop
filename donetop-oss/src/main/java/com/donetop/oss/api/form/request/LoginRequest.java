package com.donetop.oss.api.form.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.util.StringUtils;

@Getter @Setter
public class LoginRequest {

	private String username;
	private String password;

	public boolean isEmpty() {
		return !StringUtils.hasLength(username) || !StringUtils.hasLength(password);
	}

	public UsernamePasswordAuthenticationToken toUsernamePasswordAuthenticationToken() {
		return new UsernamePasswordAuthenticationToken(this.username, this.password);
	}

}
