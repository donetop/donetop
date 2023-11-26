package com.donetop.dto.draft;

import com.donetop.dto.folder.DraftFolderDTO;
import com.donetop.dto.payment.PaymentInfoDTO;
import com.donetop.enums.common.EnumDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

	private String inChargeName;

	private String email;

	private String phoneNumber;

	private String categoryName;

	private EnumDTO draftStatus;

	private String address;

	private String detailAddress;

	private long price;

	private EnumDTO paymentMethod;

	private String memo;

	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createTime;

	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime updateTime;

	private List<DraftFolderDTO> folders = new ArrayList<>();

	private PaymentInfoDTO paymentInfo;

	private List<DraftCommentDTO> draftComments = new ArrayList<>();
}
