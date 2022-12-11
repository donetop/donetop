package com.donetop.main.service.storage;

import com.donetop.domain.entity.file.File;
import com.donetop.domain.entity.folder.Folder;

import java.util.Collection;

public interface StorageService {

	Collection<File> save(Collection<Resource> resources, Folder folder);

	boolean delete(File file);

	boolean delete(Folder folder);
}
