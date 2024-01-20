package com.donetop.common.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
public class DelegatedAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private final HandlerExceptionResolver handlerExceptionResolver;

	@Override
	public void commence(final HttpServletRequest request, final HttpServletResponse response,
						 final AuthenticationException authException) {
		handlerExceptionResolver.resolveException(request, response, null, authException);
	}

}
