package com.donetop.oss.api.notice;

import com.donetop.common.api.Response.OK;
import com.donetop.dto.notice.NoticeDTO;
import com.donetop.oss.api.notice.request.NoticeCreateRequest;
import com.donetop.oss.service.notice.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.donetop.oss.api.notice.NoticeAPIController.URI.PLURAL;
import static com.donetop.oss.api.notice.NoticeAPIController.URI.SINGULAR;

@Validated
@RestController
@RequiredArgsConstructor
public class NoticeAPIController {

	public static class URI {
		public static final String SINGULAR = "/api/notice";
		public static final String PLURAL = "/api/notices";
	}

	private final NoticeService noticeService;

	@GetMapping(PLURAL)
	public ResponseEntity<OK<List<NoticeDTO>>> notices() {
		return ResponseEntity.ok(OK.of(noticeService.notices()));
	}

	@PostMapping(SINGULAR)
	public ResponseEntity<OK<Long>> create(@Valid final NoticeCreateRequest request) {
		return ResponseEntity.ok(OK.of(noticeService.createNewNotice(request)));
	}

	@DeleteMapping(SINGULAR + "/{id}")
	public ResponseEntity<OK<Long>> delete(@PathVariable("id") final long id) {
		return ResponseEntity.ok(OK.of(noticeService.deleteNotice(id)));
	}

}
