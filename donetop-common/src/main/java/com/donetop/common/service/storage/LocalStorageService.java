package com.donetop.common.service.storage;

import com.donetop.common.service.storage.Resource.FileSaveInfo;
import com.donetop.domain.entity.file.File;
import com.donetop.domain.entity.folder.Folder;
import com.donetop.domain.interfaces.FolderContainer;
import com.donetop.repository.file.FileRepository;
import com.donetop.repository.folder.FolderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileSystemUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class LocalStorageService implements StorageService {

	private final Storage storage;

	private final FileRepository fileRepository;

	private final FolderRepository folderRepository;

	@Override
	public void saveOrReplace(final Collection<Resource> resources, final Folder folder) {
		final boolean deleteSuccess = deleteAllFilesIn(Objects.requireNonNull(folder));
		log.info("File delete success: {}", deleteSuccess);
		if (!deleteSuccess) return;
		fileRepository.flush();
		add(resources, folder);
	}

	@Override
	public Collection<File> add(final Collection<Resource> resources, final Folder folder) {
		Objects.requireNonNull(folder);
		final List<File> saveSuccessFiles = Objects.requireNonNull(resources).stream()
			.map(resource -> resource.saveAt(folder))
			.filter(FileSaveInfo::isSuccess)
			.map(FileSaveInfo::getFile)
			.collect(Collectors.toList());
		fileRepository.saveAll(saveSuccessFiles);
		log.info("Save success files: {}", saveSuccessFiles);
		return saveSuccessFiles;
	}

	@Override
	public Folder addNewFolderOrGet(final FolderContainer folderContainer) {
		try {
			final Folder folder = Objects.requireNonNull(folderContainer).getOrNewFolder(storage.getRoot());
			final Path folderPath = Path.of(folder.getPath());
			if (folder.isNew() && !Files.exists(folderPath)) {
				Files.createDirectories(folderPath);
				folderRepository.save(folder);
				folderContainer.addFolder(folder);
				return folder;
			}
			return folder;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean deleteAllFilesIn(final Folder folder) {
		boolean deleteAll = true;
		for (final File file : Objects.requireNonNull(folder).getFiles()) {
			deleteAll &= delete(file);
		}
		if (deleteAll) folder.deleteAllFiles();
		return deleteAll;
	}

	@Override
	public boolean delete(final File file) {
		final boolean result = Path.of(Objects.requireNonNull(file).getPath()).toFile().delete();
		if (result) fileRepository.delete(file);
		return result;
	}

	@Override
	public boolean delete(final Folder folder) {
		final boolean result;
		try {
			result = FileSystemUtils.deleteRecursively(Path.of(Objects.requireNonNull(folder).getPath()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		if (result) folderRepository.delete(folder);
		return result;
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
