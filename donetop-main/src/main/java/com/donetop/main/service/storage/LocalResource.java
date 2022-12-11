package com.donetop.main.service.storage;

import com.donetop.domain.entity.file.File;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

public class LocalResource extends Resource {

	public LocalResource(final MultipartFile multipartFile) {
		super(multipartFile);
	}

	@Override
	protected File save(final File file) {
		try {
			multipartFile.transferTo(Path.of(file.getPath()));
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
		return file;
	}
}
