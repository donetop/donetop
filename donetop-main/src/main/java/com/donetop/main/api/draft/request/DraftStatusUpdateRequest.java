package com.donetop.main.api.draft.request;

import com.donetop.domain.entity.draft.Draft;
import com.donetop.enums.draft.DraftStatus;
import com.donetop.enums.validation.NameOfEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import static com.donetop.common.api.Message.SAME_DRAFT_STATUS;

@Getter @Setter
public class DraftStatusUpdateRequest {

	@NameOfEnum(enumClass = DraftStatus.class, message = "유효하지 않은 상태값입니다.")
	private String draftStatus;

	public Draft applyTo(final Draft draft) {
		final DraftStatus prevStatus = draft.getDraftStatus();
		final DraftStatus targetStatus = DraftStatus.valueOf(this.draftStatus);
		if (prevStatus == targetStatus) throw new IllegalStateException(SAME_DRAFT_STATUS);
		return draft.updateDraftStatus(targetStatus).setUpdateTime(LocalDateTime.now());
	}

}
