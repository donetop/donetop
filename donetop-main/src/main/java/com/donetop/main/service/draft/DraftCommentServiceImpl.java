package com.donetop.main.service.draft;

import com.donetop.common.service.storage.Resource;
import com.donetop.common.service.storage.StorageService;
import com.donetop.domain.entity.draft.DraftComment;
import com.donetop.domain.entity.draft.Draft;
import com.donetop.domain.entity.folder.Folder;
import com.donetop.main.api.draft.request.DraftCommentCreateRequest;
import com.donetop.main.service.user.UserService;
import com.donetop.repository.draft.DraftCommentRepository;
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
public class DraftCommentServiceImpl implements DraftCommentService {

	private final StorageService<Folder> storageService;

	private final DraftRepository draftRepository;

	private final DraftCommentRepository draftCommentRepository;

	private final UserService userService;

	private final String UNKNOWN_DRAFT_MESSAGE = "존재하지 않는 시안입니다. id: %s";

	private final String UNKNOWN_COMMENT_MESSAGE = "존재하지 않는 댓글입니다. id: %s";

	@Override
	public long createNewDraftComment(final DraftCommentCreateRequest request) {
		final Draft draft = draftRepository.findById(request.getDraftId())
			.orElseThrow(() -> new IllegalStateException(String.format(UNKNOWN_DRAFT_MESSAGE, request.getDraftId())));
		final DraftComment draftComment = draftCommentRepository.save(DraftComment.of(request.getContent(), draft));
		final List<Resource> resources = request.getResources();
		if (!resources.isEmpty()) {
			storageService.saveOrReplace(resources, storageService.addNewFolderOrGet(draftComment));
		}
		log.info("[CREATE] draftCommentId: {}", draftComment.getId());
		return draftComment.getId();
	}

	@Override
	public long deleteDraftComment(final long id, final User user) {
		final DraftComment draftComment = draftCommentRepository.findById(id)
			.orElseThrow(() -> new IllegalStateException(String.format(UNKNOWN_COMMENT_MESSAGE, id)));
		if (!userService.findUserBy(Objects.requireNonNull(user).getUsername()).isAdmin())
			throw new IllegalStateException("허용되지 않은 요청입니다.");
		if (draftComment.hasFolder()) storageService.delete(draftComment.getFolder());
		draftCommentRepository.delete(draftComment);
		log.info("[DELETE] draftCommentId: {}", draftComment.getId());
		return id;
	}

	@Override
	public long delete(final DraftComment draftComment) {
		if (draftComment.hasFolder()) storageService.delete(draftComment.getFolder());
		draftCommentRepository.delete(draftComment);
		log.info("[DELETE] draftCommentId: {}", draftComment.getId());
		return draftComment.getId();
	}
}
