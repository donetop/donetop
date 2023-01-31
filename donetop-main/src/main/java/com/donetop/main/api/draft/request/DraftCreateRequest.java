package com.donetop.main.api.draft.request;

import com.donetop.domain.entity.draft.Draft;
import com.donetop.enums.payment.PaymentMethod;
import com.donetop.enums.validation.ValueOfEnum;
import com.donetop.main.api.common.MultipartFilesRequest;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter @Setter
public class DraftCreateRequest extends MultipartFilesRequest {

	@NotEmpty(message = "고객명을 입력해주세요.")
	private String customerName;

	@NotNull(message = "회사 이름을 입력해주세요.")
	private String companyName;

	@NotEmpty(message = "이메일을 입력해주세요.")
	private String email;

	@NotEmpty(message = "카테고리를 선택해주세요.")
	private String category;

	@NotEmpty(message = "연락처를 입력해주세요.")
	private String phoneNumber;

	@NotEmpty(message = "주소를 입력해주세요.")
	private String address;

	@NotNull(message = "메모를 입력해주세요.")
	private String memo;

	@NotEmpty(message = "비밀번호를 입력해주세요.")
	private String password;

	@ValueOfEnum(enumClass = PaymentMethod.class, message = "유효하지 않은 결제 방법입니다.")
	private String paymentMethod;

	public Draft toEntity() {
		return new Draft().toBuilder()
			.customerName(this.customerName)
			.companyName(this.companyName)
			.email(this.email)
			.phoneNumber(this.phoneNumber)
			.category(this.category)
			.address(this.address)
			.memo(this.memo)
			.password(this.password)
			.paymentMethod(PaymentMethod.of(this.paymentMethod)).build();
	}

}
