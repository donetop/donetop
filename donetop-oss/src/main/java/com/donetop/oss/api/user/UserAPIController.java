package com.donetop.oss.api.user;

import com.donetop.common.api.Response;
import com.donetop.common.api.Response.BadRequest;
import com.donetop.common.api.Response.OK;
import com.donetop.oss.api.user.session.Session;
import com.donetop.oss.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.donetop.common.api.Message.NO_SESSION;
import static com.donetop.oss.api.user.UserAPIController.URI.SINGULAR;

@Validated
@RestController
@RequiredArgsConstructor
public class UserAPIController {

	public static class URI {
		public static final String SINGULAR = "/api/user";
	}

	private final UserService userService;

	@GetMapping(value = SINGULAR)
	public ResponseEntity<Response> userInfo(@Session final User user) {
		if (user == null) return ResponseEntity.badRequest().body(BadRequest.of(NO_SESSION));
		return ResponseEntity.ok(OK.of(userService.findUserBy(user.getUsername())));
	}
}
