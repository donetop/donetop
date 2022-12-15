package com.donetop.domain.entity.folder;

import com.donetop.domain.entity.file.File;
import com.donetop.enums.file.Extension;
import com.donetop.enums.folder.FolderType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class FolderFilesIntegrityTest {

	@Test
	void include_duplicatedFiles_impossible() {
	    //given
		final Folder folder = Folder.builder().path("/myPath").folderType(FolderType.DRAFT).build();
		final File file1 = File.builder().name("my file").extension(Extension.JPEG).folder(folder).build();
		final File file2 = File.builder().name("my file").extension(Extension.JPEG).folder(folder).build();

		//when
		folder.add(file1, file2);

	    //then
		assertThat(folder.getFiles().size()).isEqualTo(1);
	}

}
