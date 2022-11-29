package com.donetop.main.service.draft;

import com.donetop.domain.entity.draft.Draft;
import com.donetop.main.api.draft.request.DraftCreateRequest;

import java.util.List;

public interface DraftService {

	Draft createNewDraft(DraftCreateRequest request);

	List<Draft> getAll();
}
