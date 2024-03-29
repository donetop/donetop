package com.donetop.common.service.storage;

import com.donetop.domain.entity.file.File;
import com.donetop.domain.entity.folder.Folder;
import com.donetop.domain.interfaces.MultipleFolderContainer;
import com.donetop.domain.interfaces.SingleFolderContainer;
import com.donetop.enums.folder.FolderType;

import java.util.Collection;
import java.util.List;

public interface StorageService<T extends Folder> {

	void saveOrReplace(Collection<Resource> resources, T folder);

	List<File> add(Collection<Resource> resources, T folder);

	T addNewFolderOrGet(SingleFolderContainer<T> folderContainer);

	T addNewFolderOrGet(MultipleFolderContainer<T> folderContainer, FolderType folderType);

	boolean deleteAllFilesIn(T folder);

	boolean delete(File file);

	boolean delete(T folder);

}
