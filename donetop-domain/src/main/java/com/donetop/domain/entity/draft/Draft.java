package com.donetop.domain.entity.draft;

import com.donetop.domain.entity.folder.Folder;
import com.donetop.dto.draft.DraftDTO;
import com.donetop.enums.draft.Category;
import com.donetop.enums.draft.DraftStatus;
import com.donetop.enums.folder.FolderType;
import com.donetop.enums.draft.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "tbDraft")
@DynamicUpdate
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Draft implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, updatable = false)
	private long id;

	@Column(nullable = false, columnDefinition = "varchar(128) default ''")
	private String customerName;

	@Builder.Default
	@Column(nullable = false, columnDefinition = "varchar(128) default ''")
	private String companyName = "";

	@Builder.Default
	@Column(nullable = false, columnDefinition = "varchar(128) default ''")
	private String inChargeName = "";

	@Column(nullable = false, columnDefinition = "varchar(128) default ''")
	private String email;

	@Column(nullable = false, columnDefinition = "varchar(15) default ''")
	private String phoneNumber;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, columnDefinition = "varchar(64) default ''")
	private Category category;

	@Builder.Default
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, columnDefinition = "varchar(50) default ''")
	private DraftStatus draftStatus = DraftStatus.HOLDING;

	@Column(nullable = false, columnDefinition = "varchar(256) default ''")
	private String address;

	@Column(nullable = false, columnDefinition = "bigint(20) default 0")
	private long price;

	@Builder.Default
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, columnDefinition = "varchar(50) default ''")
	private PaymentMethod paymentMethod = PaymentMethod.CASH;

	@Builder.Default
	@Column(nullable = false, columnDefinition = "varchar(3000) default ''")
	private String memo = "";

	@Column(nullable = false, columnDefinition = "varchar(512) default ''")
	private String password;

	@Builder.Default
	@Column(nullable = false)
	private LocalDateTime createTime = LocalDateTime.now();

	@Builder.Default
	@Column(nullable = false)
	private LocalDateTime updateTime = LocalDateTime.now();

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "folderId")
	private Folder folder;

	public Draft updateCustomerName(final String customerName) {
		this.customerName = customerName;
		return this;
	}

	public Draft updateCompanyName(final String companyName) {
		this.companyName = companyName;
		return this;
	}

	public Draft updateInChargeName(final String inChargeName) {
		this.inChargeName = inChargeName;
		return this;
	}

	public Draft updateEmail(final String email) {
		this.email = email;
		return this;
	}

	public Draft updatePhoneNumber(final String phoneNumber) {
		this.phoneNumber = phoneNumber;
		return this;
	}

	public Draft updateCategory(final Category category) {
		this.category = category;
		return this;
	}

	public Draft updateDraftStatus(final DraftStatus draftStatus) {
		this.draftStatus = draftStatus;
		return this;
	}

	public Draft updateAddress(final String address) {
		this.address = address;
		return this;
	}

	public Draft updatePrice(final long price) {
		this.price = price;
		return this;
	}

	public Draft updatePaymentMethod(final PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
		return this;
	}

	public Draft updateMemo(final String memo) {
		this.memo = memo;
		return this;
	}

	public Draft updatePassword(final String password) {
		this.password = password;
		return this;
	}

	public Draft setUpdateTime(final LocalDateTime localDateTime) {
		this.updateTime = localDateTime;
		return this;
	}

	public void addFolder(final Folder folder) {
		this.folder = folder;
	}

	public Folder getOrNewFolder(final String root) {
		return this.folder == null ? Folder.of(FolderType.DRAFT, root, this.id) : this.folder;
	}

	public DraftDTO toDTO(final boolean includeFolder) {
		final DraftDTO draftDTO = DraftDTO.builder()
			.id(this.id)
			.customerName(this.customerName)
			.companyName(this.companyName)
			.inChargeName(this.inChargeName)
			.email(this.email)
			.phoneNumber(this.phoneNumber)
			.category(this.category.value())
			.draftStatus(this.draftStatus.value())
			.address(this.address)
			.price(this.price)
			.paymentMethod(this.paymentMethod.value())
			.memo(this.memo)
			.createTime(this.createTime)
			.updateTime(this.updateTime).build();
		if (includeFolder && this.folder != null) draftDTO.setFolder(this.folder.toDTO());
		return draftDTO;
	}
}
