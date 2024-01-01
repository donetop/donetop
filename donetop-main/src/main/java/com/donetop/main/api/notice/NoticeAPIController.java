package com.donetop.main.api.notice;

import com.donetop.common.api.Response.OK;
import com.donetop.dto.notice.NoticeDTO;
import com.donetop.main.service.notice.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.donetop.main.api.notice.NoticeAPIController.URI.PLURAL;

@RestController
@RequiredArgsConstructor
public class NoticeAPIController {

	public static class URI {
		public static final String PLURAL = "/api/notices";
	}

	private final NoticeService noticeService;

	@GetMapping(PLURAL)
	public ResponseEntity<OK<List<NoticeDTO>>> notices() {
		return ResponseEntity.ok(OK.of(noticeService.notices()));
	}

}
