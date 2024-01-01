package com.donetop.oss.api.form.handler;

import com.donetop.common.api.Response;
import com.donetop.common.api.Response.BadRequest;
import com.donetop.common.api.Response.OK;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static com.donetop.common.api.Message.LOGOUT;
import static com.donetop.common.api.Message.NO_SESSION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@Component
@RequiredArgsConstructor
public class LogoutSuccessHandler implements org.springframework.security.web.authentication.logout.LogoutSuccessHandler {

	private final ObjectMapper objectMapper;

	@Override
	public void onLogoutSuccess(final HttpServletRequest request, final HttpServletResponse response,
								final Authentication authentication) throws IOException {
		log.debug("Logout success. auth : {}", authentication);
		Response resp = authentication == null ? BadRequest.of(NO_SESSION) : OK.of(LOGOUT);
		response.setStatus(resp.getCode());
		response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
		response.setContentType(APPLICATION_JSON_VALUE);
		objectMapper.writeValue(response.getWriter(), resp);
	}
}
