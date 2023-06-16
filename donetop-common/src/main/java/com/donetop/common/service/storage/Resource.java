package com.donetop.common.service.storage;

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
			.size(this.multipartFile.getSize())
			.folder(folder)
			.build();
		return save(file);
	}

	protected abstract FileSaveInfo save(File file);

	@Getter
	@RequiredArgsConstructor
	public static class FileSaveInfo {
		private final boolean success;
		private final String message;
		private final File file;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Resource resource = (Resource) o;
		return originalFilename.equals(resource.originalFilename);
	}

	@Override
	public int hashCode() {
		return Objects.hash(originalFilename);
	}
}
