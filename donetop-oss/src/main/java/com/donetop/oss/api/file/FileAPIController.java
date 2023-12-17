package com.donetop.oss.api.file;

import com.donetop.common.service.storage.StorageService;
import com.donetop.dto.file.FileDTO;
import com.donetop.oss.service.file.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FileAPIController {

	public static class URI {
		public static final String SINGULAR = "/api/file";
	}

	private final FileService fileService;

	private final StorageService storageService;

	@GetMapping(value = URI.SINGULAR + "/{id}")
	public ResponseEntity<InputStreamResource> get(@PathVariable("id") final long id) {
		final FileDTO fileDTO = fileService.getFile(id);
		return ResponseEntity.ok()
			.contentType(MediaType.valueOf(fileDTO.getMimeType()))
			.body(storageService.read(fileDTO.getPath()));
	}

}
