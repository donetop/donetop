package com.donetop.main.service.comment;

import com.donetop.common.service.storage.Resource;
import com.donetop.common.service.storage.StorageService;
import com.donetop.domain.entity.comment.Comment;
import com.donetop.domain.entity.draft.Draft;
import com.donetop.domain.entity.folder.Folder;
import com.donetop.main.api.comment.request.CommentCreateRequest;
import com.donetop.main.service.user.UserService;
import com.donetop.repository.comment.CommentRepository;
import com.donetop.repository.draft.DraftRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

	private final StorageService<Folder> storageService;

	private final DraftRepository draftRepository;

	private final CommentRepository commentRepository;

	private final UserService userService;

	private final String UNKNOWN_DRAFT_MESSAGE = "존재하지 않는 시안입니다. id: %s";

	private final String UNKNOWN_COMMENT_MESSAGE = "존재하지 않는 댓글입니다. id: %s";

	@Override
	public long createNewComment(final CommentCreateRequest request) {
		final Draft draft = draftRepository.findById(request.getDraftId())
			.orElseThrow(() -> new IllegalStateException(String.format(UNKNOWN_DRAFT_MESSAGE, request.getDraftId())));
		final Comment comment = commentRepository.save(Comment.of(request.getContent(), draft));
		final List<Resource> resources = request.getResources();
		if (!resources.isEmpty()) {
			storageService.saveOrReplace(resources, storageService.addNewFolderOrGet(comment));
		}
		log.info("[CREATE] commentId: {}", comment.getId());
		return comment.getId();
	}

	@Override
	public long deleteComment(final long id, final User user) {
		final Comment comment = commentRepository.findById(id)
			.orElseThrow(() -> new IllegalStateException(String.format(UNKNOWN_COMMENT_MESSAGE, id)));
		if (!userService.findUserBy(Objects.requireNonNull(user).getUsername()).isAdmin())
			throw new IllegalStateException("허용되지 않은 요청입니다.");
		if (comment.hasFolder()) storageService.delete(comment.getFolder());
		commentRepository.delete(comment);
		log.info("[DELETE] commentId: {}", comment.getId());
		return id;
	}

	@Override
	public long delete(final Comment comment) {
		if (comment.hasFolder()) storageService.delete(comment.getFolder());
		commentRepository.delete(comment);
		log.info("[DELETE] commentId: {}", comment.getId());
		return comment.getId();
	}
}
