package com.donetop.main.service.file;

import com.donetop.dto.file.FileDTO;
import com.donetop.repository.file.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

	private final FileRepository fileRepository;

	@Override
	public FileDTO getFile(final long id) {
		return fileRepository.findById(id)
			.orElseThrow(() -> new IllegalStateException("File not found. id : " + id))
			.toDTO();
	}
}