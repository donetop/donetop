package com.donetop.main.api.user;

import com.donetop.common.api.Response;
import com.donetop.common.api.Response.BadRequest;
import com.donetop.common.api.Response.OK;
import com.donetop.main.api.user.request.UserCreateRequest;
import com.donetop.main.api.user.session.Session;
import com.donetop.main.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.donetop.main.api.user.UserAPIController.URI.*;

@Validated
@RestController
@RequiredArgsConstructor
public class UserAPIController {

	public static class URI {
		public static final String SINGULAR = "/api/user";
		public static final String PLURAL = "/api/users";
	}

	private final UserService userService;

	@PostMapping(value = SINGULAR)
	public ResponseEntity<OK<Long>> create(@Valid @RequestBody final UserCreateRequest request) {
		return ResponseEntity.ok(OK.of(userService.createNewUser(request)));
	}

	@GetMapping(value = SINGULAR)
	public ResponseEntity<Response> userInfo(@Session final User user) {
		if (user == null) return ResponseEntity.badRequest().body(BadRequest.of("유효한 세션 정보가 없습니다."));
		return ResponseEntity.ok(OK.of(userService.findUserBy(user.getUsername())));
	}
}
