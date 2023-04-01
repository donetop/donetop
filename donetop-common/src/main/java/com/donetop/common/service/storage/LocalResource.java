package com.donetop.common.service.storage;

import com.donetop.domain.entity.file.File;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

@Slf4j
public class LocalResource extends Resource {

	public LocalResource(final MultipartFile multipartFile) {
		super(multipartFile);
	}

	@Override
	protected FileSaveInfo save(final File file) {
		try {
			multipartFile.transferTo(Path.of(file.getPath()));
		} catch (final IOException e) {
			final String reason = e.getMessage();
			log.info("File save failed... reason : {}", reason);
			return new FileSaveInfo(false, reason, file);
		}
		return new FileSaveInfo(true, "success", file);
	}
}
