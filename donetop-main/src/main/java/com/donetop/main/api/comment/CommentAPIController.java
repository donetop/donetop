package com.donetop.main.api.comment;

import com.donetop.common.api.Response;
import com.donetop.common.api.Response.BadRequest;
import com.donetop.common.api.Response.OK;
import com.donetop.main.api.comment.request.CommentCreateRequest;
import com.donetop.main.api.user.session.Session;
import com.donetop.main.service.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.donetop.main.api.comment.CommentAPIController.URI.SINGULAR;

@Validated
@RestController
@RequiredArgsConstructor
public class CommentAPIController {

	public static class URI {
		public static final String SINGULAR = "/api/comment";
	}

	private final CommentService commentService;

	@PostMapping(SINGULAR)
	public ResponseEntity<OK<Long>> create(@Valid final CommentCreateRequest request) {
		return ResponseEntity.ok(OK.of(commentService.createNewComment(request)));
	}

	@DeleteMapping(SINGULAR + "/{id}")
	public ResponseEntity<Response> delete(@PathVariable("id") final long id,
										   @Session final User user) {
		if (user == null) return ResponseEntity.badRequest().body(BadRequest.of("유효한 세션 정보가 없습니다."));
		return ResponseEntity.ok(OK.of(commentService.deleteComment(id, user)));
	}

}
