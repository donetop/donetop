package com.donetop.oss.service.file;

import com.donetop.domain.entity.file.File;
import com.donetop.dto.file.FileDTO;
import com.donetop.oss.api.file.request.FileSortRequest;
import com.donetop.repository.file.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import static com.donetop.common.api.Message.UNKNOWN_FILE_WITH_ARGUMENTS;
import static java.util.stream.Collectors.toList;

@Slf4j
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

	@Override
	public List<FileDTO> sort(final FileSortRequest request) {
		final List<FileDTO> files = request.getFiles();
		files.forEach(fileDTO -> getOrThrow(fileDTO.getId()).setSequence(fileDTO.getSequence()));
		log.info("[SORT] fileIds: {}", files.stream().map(FileDTO::getId).collect(toList()));
		return files;
	}

	private File getOrThrow(final long fileId) {
		return fileRepository.findById(fileId)
			.orElseThrow(() -> new IllegalStateException(String.format(UNKNOWN_FILE_WITH_ARGUMENTS, fileId)));
	}
}
