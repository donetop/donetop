package com.donetop.main.service.draft;

import com.donetop.dto.draft.DraftDTO;
import com.donetop.main.api.draft.request.DraftCreateRequest;
import com.donetop.main.api.draft.request.DraftUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface DraftService {

	long createNewDraft(DraftCreateRequest request);

	long updateDraft(long id, DraftUpdateRequest request);

	DraftDTO getDraft(long id);

	Page<DraftDTO> getDraft(PageRequest request);
}
