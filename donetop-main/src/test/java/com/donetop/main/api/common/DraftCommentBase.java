package com.donetop.main.api.common;

import com.donetop.common.service.storage.LocalFileUtil;
import com.donetop.common.service.storage.Resource;
import com.donetop.common.service.storage.StorageService;
import com.donetop.domain.entity.draft.DraftComment;
import com.donetop.domain.entity.draft.Draft;
import com.donetop.domain.entity.folder.Folder;
import com.donetop.repository.draft.DraftCommentRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.file.Path;
import java.util.List;

public class DraftCommentBase extends DraftBase {

	@Autowired
	protected DraftCommentRepository draftCommentRepository;

	@Autowired
	protected StorageService<Folder> folderStorageService;

	protected DraftComment saveSingleDraftCommentWithoutFiles() {
		final Draft draft = saveSingleDraftWithoutFiles();
		final DraftComment draftComment = DraftComment.of("my content", draft);
		return draftCommentRepository.save(draftComment);
	}

	protected DraftComment saveSingleDraftCommentWithFiles() {
		final List<Resource> resources = LocalFileUtil.readResources(Path.of(testStorage.getSrc()));
		final Draft draft = saveSingleDraftWithoutFiles();
		final DraftComment draftComment = DraftComment.of("my content", draft);
		draftCommentRepository.save(draftComment);
		folderStorageService.saveOrReplace(resources, folderStorageService.addNewFolderOrGet(draftComment));
		return draftCommentRepository.save(draftComment);
	}

}
