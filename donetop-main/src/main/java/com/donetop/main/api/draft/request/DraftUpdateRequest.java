package com.donetop.main.api.draft.request;

import com.donetop.domain.entity.draft.Draft;
import com.donetop.enums.draft.DraftStatus;
import com.donetop.enums.payment.PaymentMethod;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter @Setter
public class DraftUpdateRequest {

	@NotEmpty(message = "고객명을 입력해주세요.")
	private String customerName;

	@NotNull(message = "주소를 입력해주세요.")
	private String address;

	@Min(value = 1000L, message = "최소 가격은 1,000원입니다.")
	private long price;

	@NotNull(message = "메모를 입력해주세요.")
	private String memo;

	@NotEmpty(message = "비밀번호를 입력해주세요.")
	private String password;

	@NotNull(message = "유효하지 않은 상태값입니다.")
	private DraftStatus draftStatus;

	@NotNull(message = "유효하지 않은 결제 방법입니다.")
	private PaymentMethod paymentMethod;

	public Draft applyTo(final Draft draft) {
		return draft
			.updateCustomerName(this.customerName)
			.updateDraftStatus(this.draftStatus)
			.updateAddress(this.address)
			.updatePrice(this.price)
			.updatePaymentMethod(this.paymentMethod)
			.updateMemo(this.memo)
			.updatePassword(this.password)
			.updateUpdateTime(LocalDateTime.now());
	}
}
