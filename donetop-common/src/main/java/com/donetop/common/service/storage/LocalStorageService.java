package com.donetop.common.service.storage;

import com.donetop.common.service.storage.Resource.FileSaveInfo;
import com.donetop.domain.entity.file.File;
import com.donetop.domain.entity.folder.Folder;
import com.donetop.domain.interfaces.FolderContainer;
import com.donetop.domain.interfaces.MultipleFolderContainer;
import com.donetop.domain.interfaces.SingleFolderContainer;
import com.donetop.enums.folder.FolderType;
import com.donetop.repository.file.FileRepository;
import com.donetop.repository.folder.FolderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileSystemUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import static com.donetop.common.api.Message.DUPLICATED_FILE;
import static com.donetop.common.service.storage.LocalFileUtil.readFiles;
import static java.util.stream.Collectors.toSet;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class LocalStorageService<T extends Folder> implements StorageService<T> {

	private final Storage storage;

	private final FileRepository fileRepository;

	private final FolderRepository folderRepository;

	@Override
	public void saveOrReplace(final Collection<Resource> resources, final T folder) {
		final boolean deleteSuccess = deleteAllFilesIn(Objects.requireNonNull(folder));
		log.info("File delete success: {}", deleteSuccess);
		if (!deleteSuccess) return;
		fileRepository.flush();
		add(resources, folder);
	}

	@Override
	public List<File> add(final Collection<Resource> resources, final T folder) {
		checkDuplication(resources, folder);
		final List<File> temp = new ArrayList<>();
        try {
			Objects.requireNonNull(folder);
			final List<File> saveSuccessFiles = Objects.requireNonNull(resources).stream()
				.map(resource -> resource.saveAt(folder))
				.filter(FileSaveInfo::isSuccess)
				.map(FileSaveInfo::getFile).collect(Collectors.toList());
			temp.addAll(saveSuccessFiles);
			fileRepository.saveAll(saveSuccessFiles);
			log.info("Save success files: {}", saveSuccessFiles);
			return saveSuccessFiles;
		} catch (final Exception e) {
			log.warn("Save failed. reason: {}", e.getMessage());
			temp.forEach(file -> LocalFileUtil.delete(Path.of(file.getPath())));
			throw e;
		}
	}

	private void checkDuplication(final Collection<Resource> resources, final T folder) {
		final Set<String> fileNameSet = readFiles(Path.of(folder.getPath()))
			.stream().map(java.io.File::getName).collect(toSet());
		if (resources.stream().anyMatch(resource -> fileNameSet.contains(resource.getOriginalFilename())))
			throw new IllegalStateException(DUPLICATED_FILE);
	}

	@Override
	public T addNewFolderOrGet(final SingleFolderContainer<T> folderContainer) {
		final T folder = Objects.requireNonNull(folderContainer).getFolderOrNew(storage.getRoot());
		return createDirectoriesAndPersistIfNotExist(folderContainer, folder);
	}

	@Override
	public T addNewFolderOrGet(final MultipleFolderContainer<T> folderContainer, final FolderType folderType) {
		final T folder = Objects.requireNonNull(folderContainer).getFolderOrNew(storage.getRoot(), folderType);
		return createDirectoriesAndPersistIfNotExist(folderContainer, folder);
	}

	private T createDirectoriesAndPersistIfNotExist(final FolderContainer<T> folderContainer, final T folder) {
		try {
			final Path folderPath = Path.of(folder.getPath());
			if (folder.isNew() && !Files.exists(folderPath)) {
				Files.createDirectories(folderPath);
				folderRepository.save(folder);
				folderContainer.addFolder(folder);
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

}
