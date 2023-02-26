package com.donetop.domain.entity.payment;

import com.donetop.dto.payment.PaymentHistoryDTO;
import com.donetop.dto.payment.PaymentInfoDTO;
import com.donetop.enums.payment.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Entity
@Table(name = "tbPaymentInfo")
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class PaymentInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, updatable = false)
	private long id;

	@Enumerated(value = EnumType.STRING)
	@Column(nullable = false, columnDefinition = "varchar(10) default ''")
	private PaymentStatus paymentStatus;

	@OneToMany(mappedBy = "paymentInfo", cascade = CascadeType.REMOVE)
	private final List<PaymentHistory> histories = new ArrayList<>();

	public static PaymentInfo of(final PaymentStatus paymentStatus) {
		return new PaymentInfo().toBuilder().paymentStatus(paymentStatus).build();
	}

	public boolean isNew() {
		return this.id == 0L;
	}

	public PaymentInfoDTO toDTO() {
		PaymentInfoDTO paymentInfoDTO = new PaymentInfoDTO();
		paymentInfoDTO.setId(this.id);
		paymentInfoDTO.setPaymentStatus(this.paymentStatus);
		List<PaymentHistoryDTO> historyDTOList = this.histories.stream().map(PaymentHistory::toDTO).collect(toList());
		if (historyDTOList.isEmpty()) throw new IllegalStateException("Payment history should exist at least one.");
		paymentInfoDTO.setHistories(historyDTOList);
		paymentInfoDTO.setLastHistory(historyDTOList.get(historyDTOList.size() - 1));
		return paymentInfoDTO;
	}

}
