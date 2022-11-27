package com.donetop.main.api.draft;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/draft")
@Validated
public class DraftAPIController {
	
	@PostMapping
	public ResponseEntity<String> create(@Valid @RequestBody final DraftCreateRequest request) {
		return ResponseEntity.ok("ok");
	}

}
