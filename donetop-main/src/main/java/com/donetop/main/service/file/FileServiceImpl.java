package com.donetop.main.service.file;

import com.donetop.dto.file.FileDTO;
import com.donetop.repository.file.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.donetop.common.api.Message.UNKNOWN_FILE_WITH_ARGUMENTS;

@Service
@Transactional
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

	private final FileRepository fileRepository;

	@Override
	public FileDTO getFile(final long id) {
		return fileRepository.findById(id)
			.orElseThrow(() -> new IllegalStateException(String.format(UNKNOWN_FILE_WITH_ARGUMENTS, id)))
			.toDTO();
	}
}
