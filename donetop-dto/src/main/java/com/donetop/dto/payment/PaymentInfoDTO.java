package com.donetop.dto.payment;

import com.donetop.enums.payment.PaymentStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
public class PaymentInfoDTO {

	private long id;

	private PaymentStatus paymentStatus;

	private PaymentHistoryDTO lastHistory;

	private List<PaymentHistoryDTO> histories;

}
