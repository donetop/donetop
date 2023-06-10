package com.donetop.collect.api.draft

import com.donetop.collect.api.draft.DraftCollectAPIController.URI.COLLECT
import com.donetop.collect.service.draft.DraftCollectService
import com.donetop.common.api.Response
import com.donetop.common.api.Response.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class DraftCollectAPIController(
	private val draftCollectService: DraftCollectService
) {

	object URI {
		const val COLLECT = "/api/collect"
	}

	@PostMapping(COLLECT)
	fun collect(@RequestBody draftCollectRequest: DraftCollectRequest): ResponseEntity<Response> {
		return ResponseEntity.ok(OK.of(draftCollectService.collect(draftCollectRequest)))
	}

}