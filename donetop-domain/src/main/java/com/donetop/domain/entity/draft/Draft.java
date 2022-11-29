package com.donetop.domain.entity.draft;

import com.donetop.dto.draft.DraftDTO;
import com.donetop.enums.draft.DraftStatus;
import com.donetop.enums.payment.PaymentMethod;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "tbDraft")
@Getter
@NoArgsConstructor
public class Draft implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, updatable = false)
	private long id;

	@Column(nullable = false, length = 128)
	private String customerName;

	@Enumerated(EnumType.ORDINAL)
	@Column(nullable = false, columnDefinition = "int default 0")
	private DraftStatus draftStatus = DraftStatus.HOLDING;

	@Column(length = 512)
	private String address;

	@Column(nullable = false)
	private long price;

	@Enumerated(EnumType.ORDINAL)
	@Column(nullable = false, columnDefinition = "int default 0")
	private PaymentMethod paymentMethod = PaymentMethod.CASH;

	@Column(length = 1024)
	private String memo;

	private final LocalDateTime createTime = LocalDateTime.now();

	@Builder
	public Draft(final String customerName, final String address, final long price, final String memo) {
		this.customerName = customerName;
		this.address = address;
		this.price = price;
		this.memo = memo;
	}

	public Draft changeCustomerName(final String customerName) {
		this.customerName = customerName;
		return this;
	}

	public Draft changeDraftStatus(final DraftStatus draftStatus) {
		this.draftStatus = draftStatus;
		return this;
	}

	public Draft changeAddress(final String address) {
		this.address = address;
		return this;
	}

	public Draft changePrice(final long price) {
		this.price = price;
		return this;
	}

	public Draft changePaymentMethod(final PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
		return this;
	}

	public Draft changeMemo(final String memo) {
		this.memo = memo;
		return this;
	}

	public DraftDTO toDTO() {
		final DraftDTO draftDTO = new DraftDTO();
		draftDTO.setId(this.id);
		draftDTO.setCustomerName(this.customerName);
		draftDTO.setDraftStatus(this.draftStatus);
		draftDTO.setAddress(this.address);
		draftDTO.setPrice(this.price);
		draftDTO.setPaymentMethod(this.paymentMethod);
		draftDTO.setMemo(this.memo);
		draftDTO.setCreateTime(this.createTime);
		return draftDTO;
	}
}
