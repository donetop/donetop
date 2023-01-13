package com.donetop.main.api.form.handler;

import com.donetop.main.api.common.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

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

	@Override
	public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response,
										final Authentication authentication) throws IOException {
		log.debug("Login success. auth : {}", authentication);
		Response.OK<String> ok = Response.OK.of(authentication.getName());
		response.setStatus(ok.getCode());
		response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
		response.setContentType(APPLICATION_JSON_VALUE);
		objectMapper.writeValue(response.getWriter(), ok);
	}
}
