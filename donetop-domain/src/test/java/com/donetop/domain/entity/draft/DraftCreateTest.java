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
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

import static com.donetop.enums.folder.FolderType.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
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
		assertThat(draft.getDraftFolders().isEmpty()).isTrue();
	}

	@Test
	void create_withFolderAndWithoutFiles_shouldSuccess() {
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

		// when
		draftRepository.save(draft);
		folderRepository.save(folder);

		// then
		assertThat(draft.getId()).isGreaterThan(0L);
		assertThat(folder.getId()).isGreaterThan(0L);
		assertThat(folder.getFiles().size()).isEqualTo(0);
	}

	@Test
	void create_withSameTypeFolders_shouldFail() {
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
		final Folder folder1 = DraftFolder.draftFolderBuilder().path("/myPath1").folderType(DRAFT_ORDER).draft(draft).build();
		final Folder folder2 = DraftFolder.draftFolderBuilder().path("/myPath2").folderType(DRAFT_ORDER).draft(draft).build();

		// when & then
		assertThrows(DataIntegrityViolationException.class, () -> {
			draftRepository.save(draft);
			folderRepository.saveAll(List.of(folder1, folder2));
		});
	}

	@Test
	void create_withDifferentTypeFolders_shouldSuccess() {
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
		final Folder folder1 = DraftFolder.draftFolderBuilder().path("/myPath1").folderType(DRAFT_ORDER).draft(draft).build();
		final Folder folder2 = DraftFolder.draftFolderBuilder().path("/myPath2").folderType(DRAFT_WORK).draft(draft).build();

		// when
		draftRepository.save(draft);
		folderRepository.saveAll(List.of(folder1, folder2));

		// then
		assertThat(draft.getId()).isGreaterThan(0L);
		assertThat(folder1.getId()).isGreaterThan(0L);
		assertThat(folder2.getId()).isGreaterThan(0L);
	}

	@Test
	void create_withFolderAndFiles_shouldSuccess() {
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

		// when
		draftRepository.save(draft);
		folderRepository.save(folder);
		fileRepository.saveAll(List.of(file1, file2));

		// then
		assertThat(draft.getId()).isGreaterThan(0L);
		assertThat(folder.getId()).isGreaterThan(0L);
		assertThat(folder.getFiles().size()).isEqualTo(2);
		assertThat(file1.getId()).isGreaterThan(0L);
		assertThat(file2.getId()).isGreaterThan(0L);
	}

}
