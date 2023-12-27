package com.donetop.main.api.draft;

import com.donetop.domain.entity.draft.Draft;
import com.donetop.dto.draft.DraftDTO;
import com.donetop.common.api.Response;
import com.donetop.common.api.Response.BadRequest;
import com.donetop.common.api.Response.OK;
import com.donetop.main.api.draft.request.DraftCopyRequest;
import com.donetop.main.api.draft.request.DraftCreateRequest;
import com.donetop.main.api.draft.request.DraftStatusUpdateRequest;
import com.donetop.main.api.draft.request.DraftUpdateRequest;
import com.donetop.main.api.user.session.Session;
import com.donetop.main.service.draft.DraftService;
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
import static com.donetop.main.api.draft.DraftAPIController.URI.*;
import static org.springframework.data.domain.Sort.Direction.*;

@Validated
@RestController
@RequiredArgsConstructor
public class DraftAPIController {

	public static class URI {
		public static final String PLURAL = "/api/drafts";
		public static final String SINGULAR = "/api/draft";
		public static final String COPY = SINGULAR + "/copy";
		public static final String STATUS = SINGULAR + "/status";
	}

	private final DraftService draftService;

	@PostMapping(value = SINGULAR)
	public ResponseEntity<OK<Long>> create(@Valid final DraftCreateRequest request) {
		return ResponseEntity.ok(OK.of(draftService.createNewDraft(request)));
	}

	@PostMapping(value = COPY)
	public ResponseEntity<OK<Long>> copy(@Valid @RequestBody final DraftCopyRequest request) {
		return ResponseEntity.ok(OK.of(draftService.copyDraft(request.getId())));
	}

	@GetMapping(value = PLURAL)
	public ResponseEntity<OK<Page<DraftDTO>>> get(@RequestParam(value = "page", defaultValue = "0") final int page,
												  @RequestParam(value = "size", defaultValue = "20") final int size,
												  @RequestParam(value = "direction", defaultValue = "desc") final String direction,
												  @RequestParam(value = "property", defaultValue = "createTime") final String property,
												  @QuerydslPredicate(root = Draft.class) final Predicate predicate) {
		final Order order = new Order(fromString(direction), property);
		final Sort sort = Sort.by(order);
		final PageRequest request = PageRequest.of(page, size, sort);
		return ResponseEntity.ok(OK.of(draftService.getDraft(predicate, request)));
	}

	@GetMapping(SINGULAR + "/{id}")
	public ResponseEntity<OK<DraftDTO>> get(@PathVariable("id") final long id,
											@RequestParam(value = "password", defaultValue = "") final String password,
											@Session final User user) {
		return ResponseEntity.ok(OK.of(draftService.getDraft(id, password, user)));
	}

	@PutMapping(SINGULAR + "/{id}")
	public ResponseEntity<OK<Long>> update(@PathVariable("id") final long id,
										   @Valid final DraftUpdateRequest request) {
		return ResponseEntity.ok(OK.of(draftService.updateDraft(id, request)));
	}

	@PutMapping(STATUS + "/{id}")
	public ResponseEntity<OK<Long>> updateStatus(@PathVariable("id") final long id,
												 @Valid @RequestBody final DraftStatusUpdateRequest request) {
		return ResponseEntity.ok(OK.of(draftService.updateDraftStatus(id, request)));
	}

	@DeleteMapping(SINGULAR + "/{id}")
	public ResponseEntity<Response> delete(@PathVariable("id") final long id,
										   @Session final User user) {
		if (user == null) return ResponseEntity.badRequest().body(BadRequest.of(NO_SESSION));
		return ResponseEntity.ok(OK.of(draftService.deleteDraft(id, user)));
	}

}
