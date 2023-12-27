package com.donetop.main.service.draft;

import com.donetop.dto.draft.DraftDTO;
import com.donetop.main.api.draft.request.DraftCreateRequest;
import com.donetop.main.api.draft.request.DraftStatusUpdateRequest;
import com.donetop.main.api.draft.request.DraftUpdateRequest;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.User;

public interface DraftService {

	long createNewDraft(DraftCreateRequest request);

	long updateDraft(long id, DraftUpdateRequest request);

	long updateDraftStatus(long id, DraftStatusUpdateRequest request);

	DraftDTO getDraft(long id, String password, User user);

	Page<DraftDTO> getDraft(Predicate predicate, PageRequest request);

	long deleteDraft(long id, User user);

	long copyDraft(long id);
}
