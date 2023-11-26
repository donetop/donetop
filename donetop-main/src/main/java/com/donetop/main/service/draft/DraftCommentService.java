package com.donetop.main.service.draft;

import com.donetop.domain.entity.draft.DraftComment;
import com.donetop.main.api.draft.request.DraftCommentCreateRequest;
import org.springframework.security.core.userdetails.User;

public interface DraftCommentService {

	long createNewDraftComment(DraftCommentCreateRequest request);

	long deleteDraftComment(long id, User user);

	long delete(DraftComment draftComment);

}
