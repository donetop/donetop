package com.donetop.main.api.post;

import com.donetop.common.api.Response;
import com.donetop.common.api.Response.BadRequest;
import com.donetop.common.api.Response.OK;
import com.donetop.domain.entity.post.CustomerPost;
import com.donetop.dto.post.CustomerPostDTO;
import com.donetop.main.api.post.request.CustomerPostCreateRequest;
import com.donetop.main.api.user.session.Session;
import com.donetop.main.service.post.CustomerPostService;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static com.donetop.common.api.Message.NO_SESSION;
import static com.donetop.main.api.post.CustomerPostAPIController.URI.PLURAL;
import static com.donetop.main.api.post.CustomerPostAPIController.URI.SINGULAR;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Validated
@RestController
@RequiredArgsConstructor
public class CustomerPostAPIController {

	public static class URI {
		public static final String PLURAL = "/api/customerposts";
		public static final String SINGULAR = "/api/customerpost";
	}

	private final CustomerPostService customerPostService;

	@PostMapping(SINGULAR)
	public ResponseEntity<OK<Long>> create(@Valid final CustomerPostCreateRequest request) {
		return ResponseEntity.ok(OK.of(customerPostService.createNewCustomerPost(request)));
	}

	@GetMapping(SINGULAR + "/{id}")
	public ResponseEntity<OK<CustomerPostDTO>> get(@PathVariable("id") final long id,
												   HttpServletRequest httpServletRequest) {
		return ResponseEntity.ok(OK.of(customerPostService.getCustomerPost(id, httpServletRequest)));
	}

	@GetMapping(PLURAL)
	public ResponseEntity<OK<Page<CustomerPostDTO>>> get(@PageableDefault(size = 20, sort = {"createTime"}, direction = DESC) Pageable pageable,
														 @QuerydslPredicate(root = CustomerPost.class) final Predicate predicate) {
		return ResponseEntity.ok(OK.of(customerPostService.getCustomerPost(predicate, pageable)));
	}

	@DeleteMapping(SINGULAR + "/{id}")
	public ResponseEntity<Response> delete(@PathVariable("id") final long id,
										   @Session final User user) {
		if (user == null) return ResponseEntity.badRequest().body(BadRequest.of(NO_SESSION));
		return ResponseEntity.ok(OK.of(customerPostService.deleteCustomerPost(id, user)));
	}

}
