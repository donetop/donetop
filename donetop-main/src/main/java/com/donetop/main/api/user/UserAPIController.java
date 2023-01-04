package com.donetop.main.api.user;

import com.donetop.main.api.common.Response.OK;
import com.donetop.main.api.user.request.UserCreateRequest;
import com.donetop.main.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.donetop.main.api.user.UserAPIController.Uri.*;

@Validated
@RestController
@RequiredArgsConstructor
public class UserAPIController {

	public static class Uri {
		public static final String PLURAL = "/api/user";
	}

	private final UserService userService;

	@PostMapping(value = PLURAL)
	public ResponseEntity<OK<Long>> create(@Valid @RequestBody final UserCreateRequest request) {
		return ResponseEntity.ok(OK.of(userService.createNewUser(request)));
	}
}
