package com.donetop.domain.entity.payment;

import com.donetop.dto.payment.PaymentHistoryDTO;
import com.donetop.dto.payment.PaymentInfoDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

@Entity
@Table(
	name = "tbPaymentInfo",
	uniqueConstraints = @UniqueConstraint(columnNames = {"lastTransactionNumber"})
)
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class PaymentInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, updatable = false)
	private long id;

	@Builder.Default
	@Column(nullable = false)
	private LocalDateTime updateTime = LocalDateTime.now();

	@Column(nullable = false, columnDefinition = "varchar(128) default ''")
	private String lastTransactionNumber;

	@OneToMany(mappedBy = "paymentInfo", cascade = CascadeType.REMOVE)
	private final List<PaymentHistory> histories = new ArrayList<>();

	public boolean isNew() {
		return this.id == 0L;
	}

	public PaymentInfo setUpdateTime(final LocalDateTime updateTime) {
		this.updateTime = updateTime;
		return this;
	}

	public PaymentInfo setLastTransactionNumber(final String lastTransactionNumber) {
		this.lastTransactionNumber = lastTransactionNumber;
		return this;
	}

	public PaymentInfoDTO toDTO() {
		PaymentInfoDTO paymentInfoDTO = new PaymentInfoDTO();
		paymentInfoDTO.setId(this.id);
		paymentInfoDTO.setUpdateTime(this.updateTime);
		paymentInfoDTO.setLastTransactionNumber(this.lastTransactionNumber);
		List<PaymentHistoryDTO> historyDTOList = this.histories.stream().sorted(comparing(PaymentHistory::getCreateTime)).map(PaymentHistory::toDTO).collect(toList());
		if (historyDTOList.isEmpty()) throw new IllegalStateException("Payment history should exist at least one.");
		paymentInfoDTO.setHistories(historyDTOList);
		paymentInfoDTO.setLastHistory(historyDTOList.get(historyDTOList.size() - 1));
		return paymentInfoDTO;
	}

}
