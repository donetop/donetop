package com.donetop.main.api.draft.request;

import com.donetop.domain.entity.draft.Draft;
import com.donetop.enums.draft.DraftStatus;
import com.donetop.enums.payment.PaymentMethod;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter @Setter
public class DraftUpdateRequest extends DraftCreateRequest {

	@Min(value = 1L, message = "잘못된 ID 값입니다.")
	private long id;

	@NotNull(message = "유효하지 않은 상태값입니다.")
	private DraftStatus draftStatus;

	@NotNull(message = "유효하지 않은 결제 방법입니다.")
	private PaymentMethod paymentMethod;

	public Draft applyTo(final Draft draft) {
		return draft
			.updateCustomerName(this.getCustomerName())
			.updateDraftStatus(this.getDraftStatus())
			.updateAddress(this.getAddress())
			.updatePrice(this.getPrice())
			.updatePaymentMethod(this.getPaymentMethod())
			.updateMemo(this.getMemo())
			.updateUpdateTime(LocalDateTime.now());
	}
}
