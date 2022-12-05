package com.donetop.main.service.draft;

import com.donetop.domain.entity.draft.Draft;
import com.donetop.main.api.draft.request.DraftCreateRequest;
import com.donetop.main.api.draft.request.DraftUpdateRequest;
import com.donetop.repository.draft.DraftRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DraftServiceImpl implements DraftService {

	private final DraftRepository draftRepository;

	private final String UNKNOWN_DRAFT_MESSAGE = "존재하지 않는 시안입니다. id: %s";

	@Override
	public Draft createNewDraft(final DraftCreateRequest request) {
		return draftRepository.save(request.toEntity());
	}

	@Override
	public Draft getDraft(final long id) {
		return draftRepository.findById(id)
			.orElseThrow(() -> new IllegalStateException(String.format(UNKNOWN_DRAFT_MESSAGE, id)));
	}

	@Override
	public Page<Draft> getDraft(final PageRequest request) {
		return draftRepository.findAll(request);
	}

	@Override
	public Draft updateDraft(final long id, final DraftUpdateRequest request) {
		final Draft draft = draftRepository.findById(id)
			.orElseThrow(() -> new IllegalStateException(String.format(UNKNOWN_DRAFT_MESSAGE, id)));
		return request.applyTo(draft);
	}
}
