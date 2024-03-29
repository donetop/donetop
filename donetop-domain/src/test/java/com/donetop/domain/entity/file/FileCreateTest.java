package com.donetop.domain.entity.file;

import com.donetop.domain.entity.folder.Folder;
import com.donetop.enums.folder.DomainType;
import com.donetop.repository.file.FileRepository;
import com.donetop.repository.folder.FolderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
public class FileCreateTest {

	@Autowired
	private FolderRepository folderRepository;

	@Autowired
	private FileRepository fileRepository;

	@Test
	void create_withDuplicatedFiles_shouldFail() {
		// given
		final Folder folder = Folder.builder().path("/myPath").domainType(DomainType.DRAFT).build();
		final File file1 = File.builder().name("my file.jpg").folder(folder).build();
		final File file2 = File.builder().name("my file.jpg").folder(folder).build();

		// when & then
		folderRepository.save(folder);
		assertThrows(DataIntegrityViolationException.class, () -> fileRepository.saveAll(List.of(file1, file2)));
	}

}
