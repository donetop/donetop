package com.donetop.common.service.storage;

import com.donetop.domain.entity.file.File;
import com.donetop.domain.entity.folder.Folder;
import com.donetop.domain.interfaces.FolderContainer;
import org.springframework.core.io.InputStreamResource;

import java.util.Collection;

public interface StorageService {

	void saveOrReplace(Collection<Resource> resources, Folder folder);

	Collection<File> add(Collection<Resource> resources, Folder folder);

	Folder addNewFolderOrGet(FolderContainer folderContainer);

	boolean deleteAllFilesIn(Folder folder);

	boolean delete(File file);

	boolean delete(Folder folder);

	InputStreamResource read(String path);
}
