package com.donetop.main.api.draft;

import com.donetop.dto.draft.DraftDTO;
import com.donetop.main.api.common.Response.OK;
import com.donetop.main.api.draft.request.DraftCreateRequest;
import com.donetop.main.api.draft.request.DraftUpdateRequest;
import com.donetop.main.service.draft.DraftService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.donetop.main.api.draft.DraftAPIController.PATH.PLURAL;
import static com.donetop.main.api.draft.DraftAPIController.PATH.SINGULAR;
import static org.springframework.data.domain.Sort.Direction.*;

@RestController
@Validated
@RequiredArgsConstructor
public class DraftAPIController {

	public static class PATH {
		public static final String PLURAL = "/drafts";
		public static final String SINGULAR = "/draft";
	}

	private final DraftService draftService;

	@PostMapping(value = SINGULAR)
	public ResponseEntity<OK<Long>> create(@Valid @RequestBody final DraftCreateRequest request) {
		return ResponseEntity.ok(OK.of(draftService.createNewDraft(request)));
	}

	@GetMapping(value = PLURAL)
	public ResponseEntity<OK<Page<DraftDTO>>> get(@RequestParam(value = "page", defaultValue = "0") final int page,
												  @RequestParam(value = "size", defaultValue = "20") final int size,
												  @RequestParam(value = "direction", defaultValue = "desc") final String direction,
												  @RequestParam(value = "property", defaultValue = "createTime") final String property) {
		final Order order = new Order(fromString(direction), property);
		final Sort sort = Sort.by(order);
		final PageRequest request = PageRequest.of(page, size, sort);
		return ResponseEntity.ok(OK.of(draftService.getDraft(request)));
	}

	@GetMapping(SINGULAR + "/{id}")
	public ResponseEntity<OK<DraftDTO>> get(@PathVariable("id") final long id) {
		return ResponseEntity.ok(OK.of(draftService.getDraft(id)));
	}

	@PutMapping(SINGULAR + "/{id}")
	public ResponseEntity<OK<Long>> update(@PathVariable("id") final long id,
										   @Valid @RequestBody final DraftUpdateRequest request) {
		return ResponseEntity.ok(OK.of(draftService.updateDraft(id, request)));
	}

}
