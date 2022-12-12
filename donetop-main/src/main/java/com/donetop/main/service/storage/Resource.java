package com.donetop.main.service.storage;

import com.donetop.domain.entity.file.File;
import com.donetop.domain.entity.folder.Folder;
import com.donetop.enums.file.Extension;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Getter
public abstract class Resource {

	protected final MultipartFile multipartFile;

	protected final String originalFilename;

	protected final String filenameWithoutExtension;

	protected final String contentType;

	protected final Extension extension;

	protected Resource(final MultipartFile multipartFile) {
		this.multipartFile = multipartFile;
		this.originalFilename = Objects.requireNonNull(multipartFile.getOriginalFilename());
		this.filenameWithoutExtension = originalFilename.contains(".") ?
			originalFilename.substring(0, originalFilename.lastIndexOf(".")) : originalFilename;
		this.contentType = multipartFile.getContentType();
		this.extension = Extension.from(originalFilename);
	}

	public File saveAt(final Folder folder) {
		final File file = File.builder()
			.name(filenameWithoutExtension)
			.extension(extension)
			.folder(folder)
			.build();
		return save(file);
	}

	protected abstract File save(File file);

	@Override
	public String toString() {
		return "Resource{" +
			"originalFilename='" + originalFilename + '\'' +
			", contentType='" + contentType + '\'' +
			", extension=" + extension +
			'}';
	}
}
