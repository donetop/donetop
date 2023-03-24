package com.donetop.oss.api.user;

import com.donetop.common.api.Response;
import com.donetop.common.api.Response.BadRequest;
import com.donetop.common.api.Response.OK;
import com.donetop.oss.api.user.session.Session;
import com.donetop.oss.service.user.OSSUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.donetop.oss.api.user.OSSUserAPIController.URI.SINGULAR;

@Validated
@RestController
@RequiredArgsConstructor
public class OSSUserAPIController {

	public static class URI {
		public static final String SINGULAR = "/api/ossUser";
	}

	private final OSSUserService ossUserService;

	@GetMapping(value = SINGULAR)
	public ResponseEntity<Response> userInfo(@Session final User user) {
		if (user == null) return ResponseEntity.badRequest().body(BadRequest.of("유효한 세션 정보가 없습니다."));
		return ResponseEntity.ok(OK.of(ossUserService.findUserBy(user.getUsername())));
	}
}
