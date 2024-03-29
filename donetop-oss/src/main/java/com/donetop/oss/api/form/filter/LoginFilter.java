package com.donetop.oss.api.form.filter;

import com.donetop.oss.api.form.request.LoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.donetop.common.api.Message.INSUFFICIENT_AUTHENTICATION;
import static com.donetop.oss.api.form.FormAPIController.URI.LOGIN;

@Slf4j
public class LoginFilter extends AbstractAuthenticationProcessingFilter {

	private final ObjectMapper objectMapper;

	public LoginFilter(final ObjectMapper objectMapper) {
		super(new AntPathRequestMatcher(LOGIN, HttpMethod.POST.name()));
		this.objectMapper = objectMapper;
	}

	@Override
	public Authentication attemptAuthentication(final HttpServletRequest request, final HttpServletResponse response) throws AuthenticationException, IOException {
		log.debug("Processing login request..");

		final String requestBody = IOUtils.toString(request.getReader());
		log.debug("request body : {}", requestBody);

		if (!StringUtils.hasLength(requestBody)) {
			throw new InsufficientAuthenticationException(INSUFFICIENT_AUTHENTICATION);
		}

		final LoginRequest loginRequest = objectMapper.readValue(requestBody, LoginRequest.class);
		if (loginRequest.isEmpty()) {
			throw new InsufficientAuthenticationException(INSUFFICIENT_AUTHENTICATION);
		}

		return this.getAuthenticationManager().authenticate(loginRequest.toUsernamePasswordAuthenticationToken());
	}

}
