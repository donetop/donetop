package com.donetop.main.api.draft.request;

import com.donetop.domain.entity.draft.Draft;
import com.donetop.enums.draft.DraftStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

import static com.donetop.common.api.Message.SAME_DRAFT_VALUE;

@Slf4j
@Getter @Setter
public class DraftPartialUpdateRequest {

	private String draftStatus;

	private String estimateContent;

	private String memo;

	public Draft applyTo(final Draft draft) {
		boolean update = false;
		if (this.draftStatus != null) {
			final String prevDraftStatus = draft.getDraftStatus().name();
			if (prevDraftStatus.equals(this.draftStatus)) throw new IllegalStateException(SAME_DRAFT_VALUE);
			draft.updateDraftStatus(DraftStatus.valueOf(this.draftStatus));
			update = true;
			log.info("[UPDATE STATUS] draftId: {}, {} => {}", draft.getId(), prevDraftStatus, this.draftStatus);
		}
		if (this.estimateContent != null) {
			final String prevEstimateContent = draft.getEstimateContent();
			if (prevEstimateContent.equals(this.estimateContent)) throw new IllegalStateException(SAME_DRAFT_VALUE);
			draft.updateEstimateContent(this.estimateContent);
			update = true;
			log.info("[UPDATE ESTIMATE CONTENT] draftId: {}, {} => {}", draft.getId(), prevEstimateContent, this.estimateContent);
		}
		if (this.memo != null) {
			final String prevMemo = draft.getMemo();
			if (prevMemo.equals(this.memo)) throw new IllegalStateException(SAME_DRAFT_VALUE);
			draft.updateMemo(this.memo);
			update = true;
			log.info("[UPDATE MEMO] draftId: {}, {} => {}", draft.getId(), prevMemo, this.memo);
		}
		if (update) draft.setUpdateTime(LocalDateTime.now());
		return draft;
	}

}
