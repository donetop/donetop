package com.donetop.domain.entity.draft;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.*;

import com.donetop.domain.entity.payment.PaymentMethod;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Entity
@Table(name = "tbDraft")
@Getter
@EqualsAndHashCode(of = {"id"})
public class Draft implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, updatable = false)
	private long id;

	@Column(nullable = false, length = 128)
	private String customerName;

	@Enumerated(EnumType.ORDINAL)
	@Column(nullable = false, columnDefinition = "int default 2")
	private DraftStatus draftStatus = DraftStatus.HOLDING;

	private LocalDateTime createTime;

	private String address;

	@Column(nullable = false)
	private long price;

	@Enumerated(EnumType.ORDINAL)
	@Column(nullable = false, columnDefinition = "int default 2")
	private PaymentMethod paymentMethod = PaymentMethod.CASH;

	@Column(length = 1024)
	private String memo;
	
}
