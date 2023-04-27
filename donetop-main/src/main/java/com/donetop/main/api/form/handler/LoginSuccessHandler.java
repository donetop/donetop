package com.donetop.main.api.form.handler;

import com.donetop.common.api.Response;
import com.donetop.main.api.form.request.LoginRequest;
import com.donetop.main.properties.ApplicationProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

	private final ObjectMapper objectMapper;

	private final ApplicationProperties applicationProperties;

	@Override
	public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response,
										final Authentication authentication) throws IOException {
		final String body = new String(((ContentCachingRequestWrapper) request).getContentAsByteArray());
		final boolean autoLogin = objectMapper.readValue(body, LoginRequest.class).isAutoLogin();
		log.debug("Login success. auto login : {}, auth : {}", autoLogin, authentication);
		if (autoLogin) { request.getSession().setMaxInactiveInterval(applicationProperties.getCustomMaxInactiveInterval()); }
		Response.OK<String> ok = Response.OK.of(authentication.getName());
		response.setStatus(ok.getCode());
		response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
		response.setContentType(APPLICATION_JSON_VALUE);
		objectMapper.writeValue(response.getWriter(), ok);
	}
}
