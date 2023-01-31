package com.donetop.dto.draft;

import com.donetop.dto.folder.FolderDTO;
import com.donetop.enums.draft.DraftStatus;
import com.donetop.enums.payment.PaymentMethod;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@JsonInclude(NON_NULL)
public class DraftDTO {

	private long id;

	private String customerName;

	private String companyName;

	private String email;

	private String phoneNumber;

	private String category;

	private DraftStatus draftStatus;

	private String address;

	private long price;

	private PaymentMethod paymentMethod;

	private String memo;

	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createTime;

	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime updateTime;

	private FolderDTO folder;
}
