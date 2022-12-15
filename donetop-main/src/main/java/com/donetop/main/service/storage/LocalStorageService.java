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
	public void save(final Collection<Resource> resources, Folder folder) {
		folder = saveIfNotExist(folder);
		deleteAllFilesIn(folder);
		final List<File> successFiles = new ArrayList<>();
		for (final Resource resource : resources) {
			successFiles.add(resource.saveAt(folder));
		}
		fileRepository.saveAll(successFiles);
	}

	@Override
	public Folder saveIfNotExist(final Folder folder) {
		try {
			final Path folderPath = Path.of(folder.getPath());
			if (folder.getId() == 0L && !Files.exists(folderPath)) {
				Files.createDirectories(folderPath);
				return folderRepository.save(folder);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return folder;
	}

	@Override
	public boolean deleteAllFilesIn(final Folder folder) {
		boolean deleteAll = true;
		for (final File file : folder.getFiles()) {
			deleteAll &= delete(file);
		}
		if (deleteAll) folder.deleteAllFiles();
		return deleteAll;
	}

	private boolean delete(final File file) {
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
