package com.donetop.oss.api.form.handler;

import com.donetop.oss.api.common.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginFailureHandler implements AuthenticationFailureHandler {

	private final ObjectMapper objectMapper;

	@Override
	public void onAuthenticationFailure(final HttpServletRequest request, final HttpServletResponse response,
										final AuthenticationException exception) throws IOException {
		log.debug("exception : {}, message : {}", exception.getClass().getName(), exception.getMessage());
		final String message = exception instanceof BadCredentialsException ? "비밀번호가 유효하지 않습니다." : exception.getMessage();
		Response.BadRequest<String> badRequest = Response.BadRequest.of(message);
		response.setStatus(badRequest.getCode());
		response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
		response.setContentType(APPLICATION_JSON_VALUE);
		objectMapper.writeValue(response.getWriter(), badRequest);
	}
}
