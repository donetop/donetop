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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.donetop.common.api.Message.NO_SESSION;
import static com.donetop.main.api.post.CustomerPostAPIController.URI.PLURAL;
import static com.donetop.main.api.post.CustomerPostAPIController.URI.SINGULAR;
import static org.springframework.data.domain.Sort.Direction.fromString;

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
	public ResponseEntity<OK<CustomerPostDTO>> get(@PathVariable("id") final long id) {
		return ResponseEntity.ok(OK.of(customerPostService.getCustomerPost(id)));
	}

	@GetMapping(PLURAL)
	public ResponseEntity<OK<Page<CustomerPostDTO>>> get(@RequestParam(value = "page", defaultValue = "0") final int page,
														 @RequestParam(value = "size", defaultValue = "20") final int size,
														 @RequestParam(value = "direction", defaultValue = "desc") final String direction,
														 @RequestParam(value = "property", defaultValue = "createTime") final String property,
														 @QuerydslPredicate(root = CustomerPost.class) final Predicate predicate) {
		final Order order = new Order(fromString(direction), property);
		final Sort sort = Sort.by(order);
		final PageRequest pageRequest = PageRequest.of(page, size, sort);
		return ResponseEntity.ok(OK.of(customerPostService.getCustomerPost(predicate, pageRequest)));
	}

	@DeleteMapping(SINGULAR + "/{id}")
	public ResponseEntity<Response> delete(@PathVariable("id") final long id,
										   @Session final User user) {
		if (user == null) return ResponseEntity.badRequest().body(BadRequest.of(NO_SESSION));
		return ResponseEntity.ok(OK.of(customerPostService.deleteCustomerPost(id, user)));
	}

}
