package com.donetop.domain.entity.payment;

import com.donetop.dto.payment.PaymentHistoryDTO;
import com.donetop.enums.payment.PGType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tbPaymentHistory")
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class PaymentHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, updatable = false)
	private long id;

	@Enumerated(value = EnumType.STRING)
	@Column(nullable = false, columnDefinition = "varchar(10) default ''")
	private PGType pgType;

	@Column(nullable = false, columnDefinition = "varchar(1024) default ''")
	private String rawData;

	@Builder.Default
	@Column(nullable = false)
	private LocalDateTime createTime = LocalDateTime.now();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "paymentInfoId", nullable = false)
	private PaymentInfo paymentInfo;

	public PaymentHistoryDTO toDTO() {
		final PaymentHistoryDTO paymentHistoryDTO = new PaymentHistoryDTO();
		paymentHistoryDTO.setId(this.id);
		paymentHistoryDTO.setPgType(this.pgType);
		paymentHistoryDTO.setDetail(this.pgType.detail(this.rawData));
		paymentHistoryDTO.setCreateTime(this.createTime);
		return paymentHistoryDTO;
	}

}
