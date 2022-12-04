package com.donetop.main.api.draft;

import com.donetop.domain.entity.draft.Draft;
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

import static com.donetop.main.api.draft.DraftAPIController.PATH.*;
import static org.springframework.data.domain.Sort.Direction.*;

@RestController
@RequestMapping(path = ROOT)
@Validated
@RequiredArgsConstructor
public class DraftAPIController {

	public static class PATH {
		public static final String ROOT = "/drafts";
	}

	private final DraftService draftService;

	@PostMapping
	public ResponseEntity<OK<DraftDTO>> create(@Valid @RequestBody final DraftCreateRequest request) {
		return ResponseEntity.ok(OK.of(draftService.createNewDraft(request).toDTO()));
	}

	@GetMapping
	public ResponseEntity<OK<Page<DraftDTO>>> get(@RequestParam(value = "page", defaultValue = "0") final int page,
												  @RequestParam(value = "size", defaultValue = "20") final int size,
												  @RequestParam(value = "direction", defaultValue = "desc") final String direction,
												  @RequestParam(value = "property", defaultValue = "createTime") final String property) {
		final Order order = new Order(fromString(direction), property);
		final Sort sort = Sort.by(order);
		final PageRequest request = PageRequest.of(page, size, sort);
		return ResponseEntity.ok(OK.of(draftService.getDraft(request).map(Draft::toDTO)));
	}

	@GetMapping("/{id}")
	public ResponseEntity<OK<DraftDTO>> get(@PathVariable("id") final long id) {
		return ResponseEntity.ok(OK.of(draftService.getDraft(id).toDTO()));
	}

	@PutMapping
	public ResponseEntity<OK<DraftDTO>> update(@Valid @RequestBody final DraftUpdateRequest request) {
		return ResponseEntity.ok(OK.of(draftService.updateDraft(request).toDTO()));
	}

}
