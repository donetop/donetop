package com.donetop.main.service.draft;

import com.donetop.domain.entity.draft.Draft;
import com.donetop.domain.entity.folder.Folder;
import com.donetop.dto.draft.DraftDTO;
import com.donetop.main.api.draft.request.DraftCreateRequest;
import com.donetop.main.api.draft.request.DraftUpdateRequest;
import com.donetop.main.properties.ApplicationProperties;
import com.donetop.main.service.storage.Resource;
import com.donetop.main.service.storage.StorageService;
import com.donetop.main.service.user.UserService;
import com.donetop.repository.draft.DraftRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Objects;

import static com.donetop.main.properties.ApplicationProperties.*;

@Service
@Transactional
public class DraftServiceImpl implements DraftService {

	private final Storage storage;

	private final StorageService storageService;

	private final DraftRepository draftRepository;

	private final UserService userService;

	public DraftServiceImpl(final ApplicationProperties applicationProperties,
							final StorageService storageService,
							final DraftRepository draftRepository,
							final UserService userService) {
		this.storage = applicationProperties.getStorage();
		this.storageService = storageService;
		this.draftRepository = draftRepository;
		this.userService = userService;
	}

	private final String UNKNOWN_DRAFT_MESSAGE = "존재하지 않는 시안입니다. id: %s";

	@Override
	public long createNewDraft(final DraftCreateRequest request) {
		final Draft newDraft = draftRepository.save(request.toEntity());
		saveResources(newDraft, request.getResources());
		return newDraft.getId();
	}

	@Override
	public DraftDTO getDraft(final long id, final String password, final User user) {
		final Draft draft = draftRepository.findById(id)
			.orElseThrow(() -> new IllegalStateException(String.format(UNKNOWN_DRAFT_MESSAGE, id)));
		if (user != null && userService.findUserBy(user.getUsername()).isAdmin()) return draft.toDTO(true);
		if (!draft.getPassword().equals(password)) throw new IllegalStateException("비밀번호가 일치하지 않습니다.");
		return draft.toDTO(true);
	}

	@Override
	public Page<DraftDTO> getDraft(final PageRequest request) {
		return draftRepository.findAll(request).map(draft -> draft.toDTO(false));
	}

	@Override
	public long updateDraft(final long id, final DraftUpdateRequest request) {
		final Draft draft = draftRepository.findById(id)
			.orElseThrow(() -> new IllegalStateException(String.format(UNKNOWN_DRAFT_MESSAGE, id)));
		saveResources(draft, request.getResources());
		return request.applyTo(draft).getId();
	}

	@Override
	public long deleteDraft(final long id, final User user) {
		final Draft draft = draftRepository.findById(id)
			.orElseThrow(() -> new IllegalStateException(String.format(UNKNOWN_DRAFT_MESSAGE, id)));
		if (!userService.findUserBy(Objects.requireNonNull(user).getUsername()).isAdmin())
			throw new IllegalStateException("허용되지 않은 요청입니다.");
		final Folder folder = draft.getFolder();
		if (folder != null) storageService.delete(folder);
		draftRepository.delete(draft);
		return id;
	}

	private void saveResources(final Draft draft, final Collection<Resource> resources) {
		final Folder folder = draft.getOrNewFolder(storage.getRoot());
		if (folder.isNew() && resources.isEmpty()) return;
		storageService.saveIfNotExist(folder);
		draft.addFolder(folder);
		storageService.save(resources, folder);
	}
}
