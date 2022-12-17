package com.donetop.main.service.storage;

import com.donetop.domain.entity.file.File;
import com.donetop.domain.entity.folder.Folder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Getter
public abstract class Resource {

	protected final MultipartFile multipartFile;

	protected final String originalFilename;

	protected Resource(final MultipartFile multipartFile) {
		this.multipartFile = multipartFile;
		this.originalFilename = Objects.requireNonNull(multipartFile.getOriginalFilename());
	}

	public FileSaveInfo saveAt(final Folder folder) {
		final File file = File.builder()
			.name(this.originalFilename)
			.folder(folder)
			.build();
		return new FileSaveInfo(save(file), file);
	}

	protected abstract boolean save(File file);

	@Getter
	@RequiredArgsConstructor
	public static class FileSaveInfo {
		private final boolean success;
		private final File file;
	}
}
