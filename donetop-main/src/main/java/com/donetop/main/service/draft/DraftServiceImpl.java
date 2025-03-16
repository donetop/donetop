package com.donetop.main.service.draft;

import com.donetop.common.service.storage.Resource;
import com.donetop.common.service.storage.StorageService;
import com.donetop.domain.entity.draft.Draft;
import com.donetop.domain.entity.draft.DraftComment;
import com.donetop.domain.entity.folder.DraftFolder;
import com.donetop.dto.draft.DraftDTO;
import com.donetop.main.api.draft.request.DraftCreateRequest;
import com.donetop.main.api.draft.request.DraftPartialUpdateRequest;
import com.donetop.main.api.draft.request.DraftUpdateRequest;
import com.donetop.main.api.draft.request.DraftsDeleteRequest;
import com.donetop.main.service.user.UserService;
import com.donetop.repository.draft.DraftRepository;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import static com.donetop.common.api.Message.*;
import static com.donetop.common.service.storage.LocalFileUtil.readResources;
import static com.donetop.enums.folder.FolderType.*;
import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class DraftServiceImpl implements DraftService {

	private final StorageService<DraftFolder> storageService;

	private final DraftRepository draftRepository;

	private final UserService userService;

	private final DraftCommentService draftCommentService;

	@Override
	public long createNewDraft(final DraftCreateRequest request) {
		final Draft newDraft = draftRepository.save(request.toEntity());
		final List<Resource> resources = request.getResources();
		if (!resources.isEmpty()) {
			storageService.saveOrReplace(resources, storageService.addNewFolderOrGet(newDraft, DRAFT_ORDER));
		}
		log.info("[CREATE] draftId: {}", newDraft.getId());
		return newDraft.getId();
	}

	@Override
	public DraftDTO getDraft(final long id, final String password, final User user) {
		final Draft draft = getOrThrow(id);
		if (user != null && userService.findUserBy(user.getUsername()).isAdmin()) return draft.toDTO(true);
		if (!draft.getPassword().equals(password)) throw new IllegalStateException(WRONG_PASSWORD);
		return draft.toDTO(true);
	}

	@Override
	public Page<DraftDTO> getDraft(final Predicate predicate, final Pageable pageable) {
		return draftRepository.findAll(predicate, pageable).map(draft -> draft.toDTO(false));
	}

	@Override
	public long updateDraft(final long id, final DraftUpdateRequest request) {
		final Draft draft = getOrThrow(id);
		final List<Resource> resources = request.getResources();
		if (draft.hasFolder(DRAFT_WORK) || !resources.isEmpty()) {
			storageService.saveOrReplace(resources, storageService.addNewFolderOrGet(draft, DRAFT_WORK));
		}
		log.info("[UPDATE] draftId: {}", draft.getId());
		return request.applyTo(draft).getId();
	}

	@Override
	public long partialUpdateDraft(final long id, final DraftPartialUpdateRequest request) {
		final Draft draft = getOrThrow(id);
		return request.applyTo(draft).getId();
	}

	@Override
	public long deleteDraft(final long id, final User user) {
		if (!userService.findUserBy(Objects.requireNonNull(user).getUsername()).isAdmin())
			throw new IllegalStateException(DISALLOWED_REQUEST);
		final Draft draft = getOrThrow(id);
		if (draft.hasFolder()) draft.getDraftFolders().forEach(storageService::delete);
		if (draft.hasDraftComment()) draft.getDraftComments().forEach(draftCommentService::delete);
		draftRepository.delete(draft);
		log.info("[DELETE] draftId: {}", id);
		return id;
	}

	@Override
	public long deleteDrafts(final DraftsDeleteRequest request, final User user) {
		if (!userService.findUserBy(Objects.requireNonNull(user).getUsername()).isAdmin())
			throw new IllegalStateException(DISALLOWED_REQUEST);
		final List<Draft> drafts = draftRepository.findAllById(request.getDraftIds());
		for (final Draft draft : drafts) {
			if (draft.hasFolder()) draft.getDraftFolders().forEach(storageService::delete);
			if (draft.hasDraftComment()) draft.getDraftComments().forEach(draftCommentService::delete);
			draftRepository.delete(draft);
			log.info("[DELETE] draftIds: {}", drafts.stream().map(Draft::getId).collect(toList()));
		}
		return drafts.size();
	}

	@Override
	public long copyDraft(final long id) {
		final Draft draft = getOrThrow(id);
		final Draft copiedDraft = draftRepository.save(draft.copy());
		log.info("[COPY] draftId: {}", id);
		return copiedDraft.getId();
	}

	@Override
	public long checkDraftComments(final long id, final User user) {
		if (!userService.findUserBy(Objects.requireNonNull(user).getUsername()).isAdmin())
			throw new IllegalStateException(DISALLOWED_REQUEST);
		final Draft draft = getOrThrow(id);
		final List<DraftComment> uncheckedDraftComments = draft.getDraftComments()
			.stream().filter(DraftComment::isNotChecked).collect(toList());
		uncheckedDraftComments.forEach(DraftComment::setChecked);
		log.info("[CHECK_COMMENT] draftId: {}, checkedCount: {}", id, uncheckedDraftComments.size());
		return id;
	}

	private Draft getOrThrow(final long id) {
		return draftRepository.findById(id)
			.orElseThrow(() -> new IllegalStateException(String.format(UNKNOWN_DRAFT_WITH_ARGUMENTS, id)));
	}

}
