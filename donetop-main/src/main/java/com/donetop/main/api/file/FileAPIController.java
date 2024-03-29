package com.donetop.main.api.file;

import com.donetop.dto.file.FileDTO;
import com.donetop.main.service.file.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import static com.donetop.main.api.file.FileAPIController.URI.SINGULAR;

@RestController
@RequiredArgsConstructor
public class FileAPIController {

	public static class URI {
		public static final String SINGULAR = "/api/file";
	}

	private final FileService fileService;

	@GetMapping(value = SINGULAR + "/{id}")
	public ResponseEntity<InputStreamResource> get(@PathVariable("id") final long id) {
		final FileDTO fileDTO = fileService.getFile(id);
		return ResponseEntity.ok()
			.contentType(MediaType.valueOf(fileDTO.getMimeType()))
			.body(fileService.read(fileDTO.getPath()));
	}

}
