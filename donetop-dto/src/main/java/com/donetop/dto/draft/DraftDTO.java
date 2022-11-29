package com.donetop.dto.draft;

import com.donetop.enums.draft.DraftStatus;
import com.donetop.enums.payment.PaymentMethod;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class DraftDTO {

	private long id;

	private String customerName;

	private DraftStatus draftStatus;

	private String address;

	private long price;

	private PaymentMethod paymentMethod;

	private String memo;

	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonProperty("date")
	private LocalDateTime createTime;

}
