package com.donetop.domain.interfaces;

import com.donetop.domain.entity.folder.Folder;

public interface FolderContainer {

	void addFolder(Folder folder);

	Folder getOrNewFolder(String root);

	boolean hasFolder();

}
