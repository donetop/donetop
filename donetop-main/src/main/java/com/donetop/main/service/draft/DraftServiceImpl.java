package com.donetop.main.service.draft;

import com.donetop.domain.entity.draft.Draft;
import com.donetop.main.api.draft.request.DraftCreateRequest;
import com.donetop.repository.draft.DraftRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DraftServiceImpl implements DraftService {

	private final DraftRepository draftRepository;

	@Override
	public Draft createNewDraft(final DraftCreateRequest request) {
		Draft draft = Draft.builder()
			.customerName(request.getCustomerName())
			.address(request.getAddress())
			.price(request.getPrice())
			.memo(request.getMemo())
			.build();
		return draftRepository.save(draft);
	}

	@Override
	public List<Draft> getAll() {
		return draftRepository.findAll();
	}
}
