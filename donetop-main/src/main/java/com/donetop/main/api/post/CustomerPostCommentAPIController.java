package com.donetop.main.api.post;

import com.donetop.common.api.Response;
import com.donetop.common.api.Response.BadRequest;
import com.donetop.common.api.Response.OK;
import com.donetop.main.api.post.request.CustomerPostCommentCreateRequest;
import com.donetop.main.api.user.session.Session;
import com.donetop.main.service.post.CustomerPostCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.donetop.main.api.post.CustomerPostCommentAPIController.URI.SINGULAR;

@Validated
@RestController
@RequiredArgsConstructor
public class CustomerPostCommentAPIController {

	public static class URI {
		public static final String SINGULAR = "/api/customerpost/comment";
	}

	private final CustomerPostCommentService customerPostCommentService;

	@PostMapping(SINGULAR)
	public ResponseEntity<OK<Long>> create(@Valid final CustomerPostCommentCreateRequest request) {
		return ResponseEntity.ok(OK.of(customerPostCommentService.createNewCustomerPostComment(request)));
	}

	@DeleteMapping(SINGULAR + "/{id}")
	public ResponseEntity<Response> delete(@PathVariable("id") final long id,
										   @Session final User user) {
		if (user == null) return ResponseEntity.badRequest().body(BadRequest.of("유효한 세션 정보가 없습니다."));
		return ResponseEntity.ok(OK.of(customerPostCommentService.deleteCustomerPostComment(id, user)));
	}

}
