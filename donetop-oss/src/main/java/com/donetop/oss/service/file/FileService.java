package com.donetop.oss.service.file;

import com.donetop.dto.file.FileDTO;
import com.donetop.oss.api.file.request.FileSortRequest;
import org.springframework.core.io.InputStreamResource;

import java.util.List;

public interface FileService {

	FileDTO getFile(long id);

	InputStreamResource read(String path);

	List<FileDTO> sort(FileSortRequest request);

}
