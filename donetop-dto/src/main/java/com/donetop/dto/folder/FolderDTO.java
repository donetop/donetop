package com.donetop.dto.folder;

import com.donetop.dto.file.FileDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
public class FolderDTO {

	private long id;

	@JsonIgnore
	private String path;

	private List<FileDTO> files;

}
