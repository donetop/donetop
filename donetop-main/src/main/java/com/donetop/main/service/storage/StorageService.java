package com.donetop.main.service.storage;

import com.donetop.domain.entity.folder.Folder;
import org.springframework.core.io.InputStreamResource;

import java.util.Collection;

public interface StorageService {

	void save(Collection<Resource> resources, Folder folder);

	Folder saveIfNotExist(Folder folder);

	boolean deleteAllFilesIn(Folder folder);

	boolean delete(Folder folder);

	InputStreamResource read(String path);
}