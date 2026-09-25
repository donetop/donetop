package com.donetop.main.api.draft;

import com.donetop.domain.entity.draft.Draft;
import com.donetop.dto.draft.DraftDTO;
import com.donetop.common.api.Response;
import com.donetop.common.api.Response.BadRequest;
import com.donetop.common.api.Response.OK;
import com.donetop.enums.user.RoleType;
import com.donetop.main.api.draft.request.*;
import com.donetop.main.api.user.session.Session;
import com.donetop.main.service.draft.DraftService;
import com.donetop.main.service.sms.PhoneVerificationService;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.stream.Collectors;

import static com.donetop.common.api.Message.*;
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
		public static final String PARTIAL = SINGULAR + "/partial";
		public static final String COMMENT_CHECK = SINGULAR + "/comment/check";
	}

	private final DraftService draftService;
	private final PhoneVerificationService phoneVerificationService;

	@PostMapping(value = SINGULAR)
	public ResponseEntity<Response> create(@Valid final DraftCreateRequest request,
										   @Session final User user) {
		if (!hasAdminRole(user)) {
			boolean isVerified = phoneVerificationService.checkAndConsumeVerification(request.getPhoneNumber());
			if (!isVerified) {
				return ResponseEntity.badRequest().body(BadRequest.of(SMS_NOT_VERIFIED));
			}
		}
		return ResponseEntity.ok(OK.of(draftService.createNewDraft(request)));
	}

	@PostMapping(value = COPY)
	public ResponseEntity<Response> copy(@Valid @RequestBody final DraftCopyRequest request,
										 @Session final User user) {
		if (!hasAdminRole(user)) return ResponseEntity.badRequest().body(BadRequest.of(DISALLOWED_REQUEST));
		return ResponseEntity.ok(OK.of(draftService.copyDraft(request.getId())));
	}

	@GetMapping(value = PLURAL)
	public ResponseEntity<OK<Page<DraftDTO>>> get(@PageableDefault(size = 20, sort = {"createTime"}, direction = DESC) Pageable pageable,
												  @QuerydslPredicate(root = Draft.class) final Predicate predicate) {
		return ResponseEntity.ok(OK.of(draftService.getDraft(predicate, pageable)));
	}

	@GetMapping(SINGULAR + "/{id}")
	public ResponseEntity<OK<DraftDTO>> get(@PathVariable("id") final long id,
											@RequestParam(value = "password", defaultValue = "") final String password,
											@Session final User user) {
		return ResponseEntity.ok(OK.of(draftService.getDraft(id, password, user)));
	}

	@PutMapping(SINGULAR + "/{id}")
	public ResponseEntity<Response> update(@PathVariable("id") final long id,
										   @Valid final DraftUpdateRequest request,
										   @Session final User user) {
		if (!hasAdminRole(user)) return ResponseEntity.badRequest().body(BadRequest.of(DISALLOWED_REQUEST));
		return ResponseEntity.ok(OK.of(draftService.updateDraft(id, request)));
	}

	@PutMapping(PARTIAL + "/{id}")
	public ResponseEntity<Response> updatePartial(@PathVariable("id") final long id,
												  @Valid @RequestBody final DraftPartialUpdateRequest request,
												  @Session final User user) {
		if (!hasAdminRole(user)) return ResponseEntity.badRequest().body(BadRequest.of(DISALLOWED_REQUEST));
		return ResponseEntity.ok(OK.of(draftService.partialUpdateDraft(id, request)));
	}

	@PutMapping(COMMENT_CHECK + "/{id}")
	public ResponseEntity<Response> checkComments(@PathVariable("id") final long id,
										   		  @Session final User user) {
		if (!hasAdminRole(user)) return ResponseEntity.badRequest().body(BadRequest.of(DISALLOWED_REQUEST));
		return ResponseEntity.ok(OK.of(draftService.checkDraftComments(id, user)));
	}

	@DeleteMapping(SINGULAR + "/{id}")
	public ResponseEntity<Response> delete(@PathVariable("id") final long id,
										   @Session final User user) {
		if (!hasAdminRole(user)) return ResponseEntity.badRequest().body(BadRequest.of(DISALLOWED_REQUEST));
		return ResponseEntity.ok(OK.of(draftService.deleteDraft(id, user)));
	}

	@PutMapping(PLURAL)
	public ResponseEntity<Response> delete(@Valid @RequestBody final DraftsDeleteRequest request,
										   @Session final User user) {
		if (!hasAdminRole(user)) return ResponseEntity.badRequest().body(BadRequest.of(DISALLOWED_REQUEST));
		return ResponseEntity.ok(OK.of(draftService.deleteDrafts(request, user)));
	}

	private boolean hasAdminRole(final User user) {
		if (user == null) return false;
		return user.getAuthorities().stream()
			.map(GrantedAuthority::getAuthority)
			.collect(Collectors.toList())
			.contains(RoleType.ADMIN.name());
	}

}
