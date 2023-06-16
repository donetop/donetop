package com.donetop.dto.folder;

import com.donetop.enums.folder.FolderType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class DraftFolderDTO extends FolderDTO {

	private FolderType folderType;

}
