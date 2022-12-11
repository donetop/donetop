package com.donetop.domain.entity.draft;

import com.donetop.domain.entity.folder.Folder;
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

	@Column(nullable = false, columnDefinition = "varchar(512) default ''")
	private String address;

	@Column(nullable = false)
	private long price;

	@Enumerated(EnumType.ORDINAL)
	@Column(nullable = false, columnDefinition = "int default 0")
	private PaymentMethod paymentMethod = PaymentMethod.CASH;

	@Column(nullable = false, columnDefinition = "varchar(1024) default ''")
	private String memo;

	@Column(nullable = false, columnDefinition = "varchar(1024) default ''")
	private String password;

	@Column(nullable = false)
	private LocalDateTime createTime = LocalDateTime.now();

	@Column(nullable = false)
	private LocalDateTime updateTime = LocalDateTime.now();

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "folderId")
	private Folder folder;

	@Builder
	public Draft(final String customerName, final String address, final long price, final String memo, final String password) {
		this.customerName = customerName;
		this.address = address;
		this.price = price;
		this.memo = memo;
		this.password = password;
	}

	// This constructor should be only used for test.
	@Builder(builderMethodName = "testBuilder", builderClassName = "DraftTestBuilder")
	public Draft(final String customerName, final String address, final long price, final String memo, final String password,
				 final long id, final LocalDateTime createTime, final LocalDateTime updateTime) {
		this(customerName, address, price, memo, password);
		this.id = id;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}

	public Draft updateCustomerName(final String customerName) {
		if (!this.customerName.equals(customerName)) this.customerName = customerName;
		return this;
	}

	public Draft updateDraftStatus(final DraftStatus draftStatus) {
		if (!this.draftStatus.equals(draftStatus)) this.draftStatus = draftStatus;
		return this;
	}

	public Draft updateAddress(final String address) {
		if (!this.address.equals(address)) this.address = address;
		return this;
	}

	public Draft updatePrice(final long price) {
		if (this.price != price) this.price = price;
		return this;
	}

	public Draft updatePaymentMethod(final PaymentMethod paymentMethod) {
		if (!this.paymentMethod.equals(paymentMethod)) this.paymentMethod = paymentMethod;
		return this;
	}

	public Draft updateMemo(final String memo) {
		if (!this.memo.equals(memo)) this.memo = memo;
		return this;
	}

	public Draft updatePassword(final String password) {
		if (!this.password.equals(password)) this.password = password;
		return this;
	}

	public Draft updateUpdateTime(final LocalDateTime localDateTime) {
		this.updateTime = localDateTime;
		return this;
	}

	public void addFolder(final Folder folder) {
		this.folder = folder;
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
		draftDTO.setUpdateTime(this.updateTime);
		return draftDTO;
	}
}
