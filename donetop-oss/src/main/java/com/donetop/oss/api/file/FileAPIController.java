package com.donetop.oss.api.file;

import com.donetop.common.api.Response.OK;
import com.donetop.dto.file.FileDTO;
import com.donetop.oss.api.file.request.FileSortRequest;
import com.donetop.oss.service.file.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static com.donetop.oss.api.file.FileAPIController.URI.*;

@Validated
@RestController
@RequiredArgsConstructor
public class FileAPIController {

	public static class URI {
		public static final String SINGULAR = "/api/file";
		public static final String PLURAL = "/api/files";
		public static final String SORT = PLURAL + "/sort";
	}

	private final FileService fileService;

	@GetMapping(value = SINGULAR + "/{id}")
	public ResponseEntity<InputStreamResource> get(@PathVariable("id") final long id) {
		final FileDTO fileDTO = fileService.getFile(id);
		return ResponseEntity.ok()
			.contentType(MediaType.valueOf(fileDTO.getMimeType()))
			.body(fileService.read(fileDTO.getPath()));
	}

	@PutMapping(value = SORT)
	public ResponseEntity<OK<List<FileDTO>>> sort(@Valid @RequestBody final FileSortRequest request) {
		return ResponseEntity.ok(OK.of(fileService.sort(request)));
	}

}
