package com.donetop.main.api.file;

import com.donetop.dto.file.FileDTO;
import com.donetop.main.service.file.FileService;
import com.donetop.main.service.storage.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLConnection;

import static com.donetop.main.api.file.FileAPIController.PATH.SINGULAR;

@RestController
@RequiredArgsConstructor
public class FileAPIController {

	public static class PATH {
		public static final String SINGULAR = "/file";
	}

	private final FileService fileService;

	private final StorageService storageService;

	@GetMapping(value = SINGULAR + "/{id}")
	public ResponseEntity<InputStreamResource> get(@PathVariable("id") final long id) {
		final FileDTO fileDTO = fileService.getFile(id);
		return ResponseEntity.ok()
			.contentType(MediaType.valueOf(URLConnection.guessContentTypeFromName(fileDTO.getFileName())))
			.body(storageService.read(fileDTO.getPath()));
	}

}
