package com.donetop.oss.api.payment.request;

import com.donetop.enums.payment.PGType;
import com.donetop.enums.validation.NameOfEnum;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;

@Getter @Setter
public class PaymentCancelRequest {

	@Min(value = 1L, message = "결제 정보가 유효하지 않습니다.")
	private long paymentInfoId;

	@NameOfEnum(enumClass = PGType.class)
	private String pgType;

	public PGType getPGType() {
		return PGType.of(this.pgType);
	}

}
