package com.donetop.main.api.draft.request;

import com.donetop.domain.entity.draft.Draft;
import com.donetop.enums.draft.DraftStatus;
import com.donetop.enums.draft.PaymentMethod;
import com.donetop.enums.validation.NameOfEnum;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter @Setter
public class DraftUpdateRequest extends DraftCreateRequest {

	@Min(value = 0L, message = "최소 가격은 0원입니다.")
	private long price;

	@NameOfEnum(enumClass = DraftStatus.class, message = "유효하지 않은 상태값입니다.")
	private String draftStatus;

	@NotNull(message = "담당자 이름을 입력해주세요.")
	private String inChargeName;

	@NotNull(message = "메모를 입력해주세요.")
	private String memo;

	public Draft applyTo(final Draft draft) {
		return draft
			.updateCustomerName(getCustomerName())
			.updateCompanyName(getCompanyName())
			.updateInChargeName(this.inChargeName)
			.updateEmail(getEmail())
			.updatePhoneNumber(getPhoneNumber())
			.updateCategoryName(getCategoryName())
			.updateDraftStatus(DraftStatus.valueOf(this.draftStatus))
			.updateAddress(getAddress())
			.updateDetailAddress(getDetailAddress())
			.updatePrice(getPrice())
			.updatePaymentMethod(PaymentMethod.valueOf(getPaymentMethod()))
			.updateMemo(getMemo())
			.updateEstimateContent(getEstimateContent())
			.updatePassword(getPassword())
			.setUpdateTime(LocalDateTime.now());
	}
}
