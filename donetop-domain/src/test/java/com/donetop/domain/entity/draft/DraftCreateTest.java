package com.donetop.domain.entity.draft;

import com.donetop.domain.entity.file.File;
import com.donetop.domain.entity.folder.Folder;
import com.donetop.enums.folder.FolderType;
import com.donetop.repository.draft.DraftRepository;
import com.donetop.repository.file.FileRepository;
import com.donetop.repository.folder.FolderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class DraftCreateTest {

	@Autowired
	private DraftRepository draftRepository;

	@Autowired
	private FolderRepository folderRepository;

	@Autowired
	private FileRepository fileRepository;

	@Test
	void create_withoutFolder_shouldSuccess() {
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

		// when
		draftRepository.save(draft);

		// then
		assertThat(draft.getId()).isGreaterThan(0L);
		assertThat(draft.getFolder()).isNull();
	}

	@Test
	void create_withFolderAndWithoutFiles_shouldSuccess() {
		// given
		final Folder folder = Folder.builder().path("/myPath").folderType(FolderType.DRAFT).build();
		final Draft draft = new Draft().toBuilder()
			.customerName("jin")
			.email("my email")
			.phoneNumber("000-0000-0000")
			.categoryName("배너")
			.address("my address")
			.detailAddress("my detail address")
			.memo("simple test")
			.password("my password").build();
		draft.addFolder(folder);

		// when
		folderRepository.save(folder);
		draftRepository.save(draft);

		// then
		assertThat(draft.getId()).isGreaterThan(0L);
		assertThat(folder.getId()).isGreaterThan(0L);
		assertThat(folder.getFiles().size()).isEqualTo(0);
	}

	@Test
	void create_withFolderAndFiles_shouldSuccess() {
		// given
		final Folder folder = Folder.builder().path("/myPath").folderType(FolderType.DRAFT).build();
		final File file1 = File.builder().name("file1.jpg").folder(folder).build();
		final File file2 = File.builder().name("file2.png").folder(folder).build();
		final Draft draft = new Draft().toBuilder()
			.customerName("jin")
			.email("my email")
			.phoneNumber("000-0000-0000")
			.categoryName("배너")
			.address("my address")
			.detailAddress("my detail address")
			.memo("simple test")
			.password("my password").build();
		draft.addFolder(folder);

		// when
		folderRepository.save(folder);
		fileRepository.saveAll(List.of(file1, file2));
		draftRepository.save(draft);

		// then
		assertThat(draft.getId()).isGreaterThan(0L);
		assertThat(folder.getId()).isGreaterThan(0L);
		assertThat(folder.getFiles().size()).isEqualTo(2);
		assertThat(file1.getId()).isGreaterThan(0L);
		assertThat(file2.getId()).isGreaterThan(0L);
	}

}
