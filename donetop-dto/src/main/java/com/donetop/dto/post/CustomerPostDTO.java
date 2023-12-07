package com.donetop.dto.post;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerPostDTO {

	private long id;

	private String customerName;

	private String title;

	private String content;

	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createTime;

	@Builder.Default
	private List<CustomerPostCommentDTO> customerPostComments = new ArrayList<>();

}
