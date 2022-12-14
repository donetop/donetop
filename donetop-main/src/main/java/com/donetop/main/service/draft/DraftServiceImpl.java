package com.donetop.main.service.draft;

import com.donetop.domain.entity.draft.Draft;
import com.donetop.domain.entity.folder.Folder;
import com.donetop.dto.draft.DraftDTO;
import com.donetop.enums.folder.FolderType;
import com.donetop.main.api.draft.request.DraftCreateRequest;
import com.donetop.main.api.draft.request.DraftUpdateRequest;
import com.donetop.main.properties.ApplicationProperties;
import com.donetop.main.service.storage.Resource;
import com.donetop.main.service.storage.StorageService;
import com.donetop.repository.draft.DraftRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.donetop.main.properties.ApplicationProperties.*;

@Slf4j
@Service
@Transactional
public class DraftServiceImpl implements DraftService {

	private final Storage storage;

	private final StorageService storageService;

	private final DraftRepository draftRepository;

	public DraftServiceImpl(final ApplicationProperties applicationProperties,
							final StorageService storageService,
							final DraftRepository draftRepository) {
		this.storage = applicationProperties.getStorage();
		this.storageService = storageService;
		this.draftRepository = draftRepository;
	}

	private final String UNKNOWN_DRAFT_MESSAGE = "존재하지 않는 시안입니다. id: %s";

	@Override
	public long createNewDraft(final DraftCreateRequest request) {
		final Draft newDraft = draftRepository.save(request.toEntity());
		final List<Resource> resources = request.getResources();
		if (!resources.isEmpty()) {
			final Folder newFolder = Folder.of(FolderType.DRAFT, storage.getRoot(), newDraft.getId());
			storageService.save(resources, newFolder);
			newDraft.addFolder(newFolder);
			log.info("[createNewDraft] Save resources : {}", resources);
		}
		return newDraft.getId();
	}

	@Override
	public DraftDTO getDraft(final long id) {
		return draftRepository.findById(id)
			.orElseThrow(() -> new IllegalStateException(String.format(UNKNOWN_DRAFT_MESSAGE, id)))
			.toDTO();
	}

	@Override
	public Page<DraftDTO> getDraft(final PageRequest request) {
		return draftRepository.findAll(request).map(Draft::toDTO);
	}

	@Override
	public long updateDraft(final long id, final DraftUpdateRequest request) {
		final Draft draft = draftRepository.findById(id)
			.orElseThrow(() -> new IllegalStateException(String.format(UNKNOWN_DRAFT_MESSAGE, id)));
		return request.applyTo(draft).getId();
	}
}
