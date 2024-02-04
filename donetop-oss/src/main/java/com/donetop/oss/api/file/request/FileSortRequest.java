package com.donetop.oss.api.file.request;

import com.donetop.dto.file.FileDTO;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class FileSortRequest {

	@NotEmpty(message = "정렬할 데이터가 없습니다.")
	private List<FileDTO> files = new ArrayList<>();

}
