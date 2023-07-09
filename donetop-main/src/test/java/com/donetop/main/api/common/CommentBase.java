package com.donetop.main.api.common;

import com.donetop.common.service.storage.LocalFileUtil;
import com.donetop.common.service.storage.Resource;
import com.donetop.common.service.storage.StorageService;
import com.donetop.domain.entity.comment.Comment;
import com.donetop.domain.entity.draft.Draft;
import com.donetop.domain.entity.folder.Folder;
import com.donetop.repository.comment.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.file.Path;
import java.util.List;

public class CommentBase extends DraftBase {

	@Autowired
	protected CommentRepository commentRepository;

	@Autowired
	protected StorageService<Folder> folderStorageService;

	protected Comment saveSingleCommentWithoutFiles() {
		final Draft draft = saveSingleDraftWithoutFiles();
		final Comment comment = Comment.of("my content", draft);
		return commentRepository.save(comment);
	}

	protected Comment saveSingleCommentWithFiles() {
		final List<Resource> resources = LocalFileUtil.readResources(Path.of(testStorage.getSrc()));
		final Draft draft = saveSingleDraftWithoutFiles();
		final Comment comment = Comment.of("my content", draft);
		commentRepository.save(comment);
		folderStorageService.saveOrReplace(resources, folderStorageService.addNewFolderOrGet(comment));
		return commentRepository.save(comment);
	}

}
