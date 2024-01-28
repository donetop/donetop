package com.donetop.common.service.storage;

import com.donetop.domain.entity.file.File;
import org.springframework.web.multipart.MultipartFile;

public class MockResource extends Resource {

	public MockResource(final MultipartFile multipartFile) {
		super(multipartFile);
	}

	@Override
	protected FileSaveInfo save(File file) {
		return new FileSaveInfo(true , "success", file);
	}

}
