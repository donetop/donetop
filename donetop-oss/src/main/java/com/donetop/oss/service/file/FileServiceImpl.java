package com.donetop.oss.service.file;

import com.donetop.dto.file.FileDTO;
import com.donetop.repository.file.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

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

	@Override
	public InputStreamResource read(final String path) {
		try {
			return new InputStreamResource(Files.newInputStream(Path.of(Objects.requireNonNull(path))));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
