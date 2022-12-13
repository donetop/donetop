package com.donetop.main.service.storage;

import com.donetop.domain.entity.file.File;
import com.donetop.domain.entity.folder.Folder;
import com.donetop.repository.file.FileRepository;
import com.donetop.repository.folder.FolderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileSystemUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class LocalStorageService implements StorageService {

	private final FileRepository fileRepository;

	private final FolderRepository folderRepository;

	@Override
	public List<File> save(final Collection<Resource> resources, final Folder folder) {
		try {
			Files.createDirectories(Path.of(folder.getPath()));
			folderRepository.save(folder);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		final List<File> successFiles = new ArrayList<>();
		for (final Resource resource : resources) {
			successFiles.add(resource.saveAt(folder));
		}
		return fileRepository.saveAll(successFiles);
	}

	@Override
	public boolean delete(final File file) {
		final boolean result = Path.of(file.getPath()).toFile().delete();
		if (result) fileRepository.delete(file);
		return result;
	}

	@Override
	public boolean delete(final Folder folder) {
		final boolean result;
		try {
			result = FileSystemUtils.deleteRecursively(Path.of(folder.getPath()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		if (result) folderRepository.delete(folder);
		return result;
	}
}
