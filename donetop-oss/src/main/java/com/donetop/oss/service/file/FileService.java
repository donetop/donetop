package com.donetop.oss.service.file;

import com.donetop.dto.file.FileDTO;
import org.springframework.core.io.InputStreamResource;

public interface FileService {

	FileDTO getFile(long id);

	InputStreamResource read(String path);

}
