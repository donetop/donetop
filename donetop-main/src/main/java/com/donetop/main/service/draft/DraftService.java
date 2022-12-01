package com.donetop.main.service.draft;

import com.donetop.domain.entity.draft.Draft;
import com.donetop.main.api.draft.request.DraftCreateRequest;
import com.donetop.main.api.draft.request.DraftUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface DraftService {

	Draft createNewDraft(DraftCreateRequest request);

	Draft updateDraft(DraftUpdateRequest request);

	Draft getDraft(long id);

	Page<Draft> getDraft(PageRequest request);
}
