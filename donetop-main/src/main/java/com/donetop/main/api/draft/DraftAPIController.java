package com.donetop.main.api.draft;

import com.donetop.domain.entity.draft.Draft;
import com.donetop.dto.draft.DraftDTO;
import com.donetop.main.api.draft.request.DraftCreateRequest;
import com.donetop.main.service.draft.DraftService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static java.util.stream.Collectors.*;

@RestController
@RequestMapping(path = "/drafts")
@Validated
@RequiredArgsConstructor
public class DraftAPIController {

	private final DraftService draftService;

	@PostMapping
	public ResponseEntity<DraftDTO> create(@Valid @RequestBody final DraftCreateRequest request) {
		return ResponseEntity.ok(draftService.createNewDraft(request).toDTO());
	}

	@GetMapping
	public ResponseEntity<List<DraftDTO>> getAll() {
		return ResponseEntity.ok(draftService.getAll().stream().map(Draft::toDTO).collect(toList()));
	}

}
