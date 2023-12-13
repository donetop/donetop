package com.donetop.main.api.form.handler;

import com.donetop.common.api.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static com.donetop.common.api.Message.WRONG_ID_OR_PASSWORD;
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
		Response.BadRequest<String> badRequest = Response.BadRequest.of(WRONG_ID_OR_PASSWORD);
		response.setStatus(badRequest.getCode());
		response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
		response.setContentType(APPLICATION_JSON_VALUE);
		objectMapper.writeValue(response.getWriter(), badRequest);
	}
}
