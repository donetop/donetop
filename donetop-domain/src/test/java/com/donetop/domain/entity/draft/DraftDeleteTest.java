package com.donetop.domain.entity.draft;

import com.donetop.domain.entity.file.File;
import com.donetop.domain.entity.folder.DraftFolder;
import com.donetop.domain.entity.folder.Folder;
import com.donetop.repository.draft.DraftRepository;
import com.donetop.repository.file.FileRepository;
import com.donetop.repository.folder.FolderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static com.donetop.enums.folder.FolderType.DRAFT_ORDER;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class DraftDeleteTest {

	@Autowired
	private DraftRepository draftRepository;

	@Autowired
	private FolderRepository folderRepository;

	@Autowired
	private FileRepository fileRepository;

	@Autowired
	private EntityManager em;

	@Test
	void delete_hasFolderAndFiles_shouldBeDeletedAll() {
		// given
		final Draft draft = new Draft().toBuilder()
			.customerName("jin")
			.email("my email")
			.phoneNumber("000-0000-0000")
			.categoryName("배너")
			.address("my address")
			.detailAddress("my detail address")
			.memo("simple test")
			.password("my password").build();
		final Folder folder = DraftFolder.draftFolderBuilder().path("/myPath").folderType(DRAFT_ORDER).draft(draft).build();
		final File file1 = File.builder().name("file1.jpg").folder(folder).build();
		final File file2 = File.builder().name("file2.png").folder(folder).build();
		draftRepository.save(draft);
		folderRepository.save(folder);
		fileRepository.saveAll(List.of(file1, file2));

		// when
		draftRepository.delete(draft);
		em.flush(); em.clear();
		Optional<Draft> retrievedDraft = draftRepository.findById(draft.getId());
		Optional<Folder> retrievedFolder = folderRepository.findById(folder.getId());
		Optional<File> retrievedFile1 = fileRepository.findById(file1.getId());
		Optional<File> retrievedFile2 = fileRepository.findById(file1.getId());

		// then
		assertThat(retrievedDraft.isPresent()).isFalse();
		assertThat(retrievedFolder.isPresent()).isFalse();
		assertThat(retrievedFile1.isPresent()).isFalse();
		assertThat(retrievedFile2.isPresent()).isFalse();
	}

}
