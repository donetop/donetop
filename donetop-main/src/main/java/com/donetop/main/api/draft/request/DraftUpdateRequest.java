package com.donetop.main.api.draft.request;

import com.donetop.domain.entity.draft.Draft;
import com.donetop.enums.draft.DraftStatus;
import com.donetop.enums.payment.PaymentMethod;
import com.donetop.enums.validation.ValueOfEnum;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import java.time.LocalDateTime;

@Getter @Setter
public class DraftUpdateRequest extends DraftCreateRequest {

	@Min(value = 1000L, message = "최소 가격은 1,000원입니다.")
	private long price;

	@ValueOfEnum(enumClass = DraftStatus.class, message = "유효하지 않은 상태값입니다.")
	private String draftStatus;

	public Draft applyTo(final Draft draft) {
		return draft
			.updateCustomerName(getCustomerName())
			.updateCompanyName(getCompanyName())
			.updateEmail(getEmail())
			.updatePhoneNumber(getPhoneNumber())
			.updateCategory(getCategory())
			.updateDraftStatus(DraftStatus.of(this.draftStatus))
			.updateAddress(getAddress())
			.updatePrice(getPrice())
			.updatePaymentMethod(PaymentMethod.of(getPaymentMethod()))
			.updateMemo(getMemo())
			.updatePassword(getPassword())
			.setUpdateTime(LocalDateTime.now());
	}
}
