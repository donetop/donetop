package com.donetop.dto.file;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class FileDTO {

	private long id;

	private String name;

	private String mimeType;

	@JsonIgnore
	private String path;

	private long size;

	private int sequence;

}
