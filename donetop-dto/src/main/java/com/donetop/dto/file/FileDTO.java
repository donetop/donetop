package com.donetop.dto.file;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class FileDTO {

	private long id;

	private String name;

	private String mimeType;

	private String path;

}
