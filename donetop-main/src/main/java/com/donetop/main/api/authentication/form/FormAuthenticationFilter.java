package com.donetop.main.api.authentication.form;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static com.donetop.main.api.authentication.form.FormAPIController.Uri.AUTHENTICATION;
import static com.donetop.main.api.common.Response.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class FormAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	private final ObjectMapper objectMapper;

	@Autowired
	public FormAuthenticationFilter(final ObjectMapper objectMapper) {
		super(new AntPathRequestMatcher(AUTHENTICATION, HttpMethod.POST.name()));
		this.objectMapper = objectMapper;
	}

	@Override
	public Authentication attemptAuthentication(final HttpServletRequest request, final HttpServletResponse response) throws AuthenticationException, IOException {
		log.debug("Processing login request..");

		final String requestBody = IOUtils.toString(request.getReader());
		log.debug("request body : {}", requestBody);

		if (!StringUtils.hasLength(requestBody)) {
			throw new InsufficientAuthenticationException("유저이름 및 비밀번호를 모두 입력해주세요.");
		}

		final FormLoginRequest formLoginRequest = objectMapper.readValue(requestBody, FormLoginRequest.class);
		if (formLoginRequest.isEmpty()) {
			throw new InsufficientAuthenticationException("유저이름 및 비밀번호를 모두 입력해주세요.");
		}

		return this.getAuthenticationManager().authenticate(formLoginRequest.toToken());
	}

	@Slf4j
	@RequiredArgsConstructor
	public static class SimpleAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

		private final ObjectMapper objectMapper;

		@Override
		public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response,
											final Authentication authentication) throws IOException {
			log.debug("auth : {}", authentication);
			OK<String> ok = OK.of(authentication.getName());
			response.setStatus(ok.getCode());
			response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
			response.setContentType(APPLICATION_JSON_VALUE);
			objectMapper.writeValue(response.getWriter(), ok);
		}
	}

	@Slf4j
	@RequiredArgsConstructor
	public static class SimpleAuthenticationFailureHandler implements AuthenticationFailureHandler {

		private final ObjectMapper objectMapper;

		@Override
		public void onAuthenticationFailure(final HttpServletRequest request, final HttpServletResponse response,
											final AuthenticationException exception) throws IOException {
			log.debug("exception : {}, message : {}", exception.getClass().getName(), exception.getMessage());
			final String message = exception instanceof BadCredentialsException ? "비밀번호가 유효하지 않습니다." : exception.getMessage();
			BadRequest<String> badRequest = BadRequest.of(message);
			response.setStatus(badRequest.getCode());
			response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
			response.setContentType(APPLICATION_JSON_VALUE);
			objectMapper.writeValue(response.getWriter(), badRequest);
		}
	}
}
