package com.donetop.common.service.storage;

import com.donetop.domain.entity.folder.Folder;
import com.donetop.enums.folder.FolderType;
import com.donetop.repository.file.FileRepository;
import com.donetop.repository.folder.FolderRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
@TestPropertySource(
	properties = {
		"src=src/test/resources/storage/src",
		"dst=src/test/resources/storage/dst"
	}
)
class LocalStorageServiceTest {

	@Mock
	private FileRepository fileRepository;

	@Mock
	private FolderRepository folderRepository;

	@InjectMocks
	private LocalStorageService localStorageService;

	@Value("${src}")
	private String src;

	@Value("${dst}")
	private String dst;

	final Answer<Object> returnFirstParameter = i -> i.getArguments()[0];

	@BeforeEach
	void beforeEach() throws IOException {
		Files.createDirectories(Path.of(dst));
	}

	@AfterEach
	void afterEach() throws IOException {
		FileSystemUtils.deleteRecursively(Path.of(dst));
	}

	@Test
	void read_filesFromSRC_shouldSuccess() {
		// given

		// when
		final File directory = Path.of(src).toFile();

		// then
		assertThat(directory.exists()).isTrue();
		assertThat(directory.isDirectory()).isTrue();
		assertThat(Objects.requireNonNull(directory.listFiles()).length).isEqualTo(4);
	}

	@Test
	void save_SrcFilesAtDst_shouldExistFilesAtDst() {
		// given
		final List<Resource> resources = LocalFileUtil.readResources(Path.of(src));
		final Folder folder = Folder.builder()
			.folderType(FolderType.DRAFT)
			.path(dst)
			.build();

		// when
		localStorageService.save(resources, folder);

		// then
		assertThat(folder.getFiles().size()).isEqualTo(resources.size());
		assertThat(Objects.requireNonNull(Path.of(dst).toFile().listFiles()).length).isEqualTo(4);
	}

	@Test
	void save_singleSrcFileMultipleTimesAtDst_shouldExistSingleFileAtDst() {
		// given
		final List<List<Resource>> resourcesList = LocalFileUtil.readResources(Path.of(src))
			.stream().map(List::of).collect(Collectors.toList());
		final Folder folder = Folder.builder()
			.folderType(FolderType.DRAFT)
			.path(dst)
			.build();

		// when
		localStorageService.save(resourcesList.get(0), folder);
		localStorageService.save(resourcesList.get(1), folder);
		localStorageService.save(resourcesList.get(2), folder);

		// then
		assertThat(folder.getFiles().size()).isEqualTo(1);
		assertThat(Objects.requireNonNull(Path.of(dst).toFile().listFiles()).length).isEqualTo(1);
	}

	@Test
	void delete_allDstFiles_shouldExistFolderAndNotExistFiles() {
		// given
		final List<Resource> resources = LocalFileUtil.readResources(Path.of(src));
		final Folder folder = Folder.builder()
			.folderType(FolderType.DRAFT)
			.path(dst)
			.build();
		given(fileRepository.saveAll(anyCollection())).will(returnFirstParameter);

		// when
		localStorageService.save(resources, folder);
		boolean deleteResult = localStorageService.deleteAllFilesIn(folder);

		// then
		assertThat(deleteResult).isTrue();
		assertThat(folder.getFiles().size()).isEqualTo(0);
		final File directory = Path.of(dst).toFile();
		assertThat(directory.exists()).isTrue();
		assertThat(Objects.requireNonNull(directory.listFiles()).length).isEqualTo(0);
	}

	@Test
	void delete_folder_shouldNotExistFolder() {
		// given
		final List<Resource> resources = LocalFileUtil.readResources(Path.of(src));
		final Folder folder = Folder.builder()
			.folderType(FolderType.DRAFT)
			.path(dst)
			.build();

		// when
		localStorageService.save(resources, folder);
		boolean deleteResult = localStorageService.delete(folder);

		// then
		assertThat(deleteResult).isTrue();
		assertThat(Path.of(dst).toFile().exists()).isFalse();
	}

	@Test
	void add_singleFile_shouldExistOne() {
		// given
		final List<Resource> resources = LocalFileUtil.readResources(Path.of(src));
		final Folder folder = Folder.builder()
			.folderType(FolderType.DRAFT)
			.path(dst)
			.build();

		// when
		localStorageService.add(resources.get(0), folder);

		// then
		final File directory = Path.of(dst).toFile();
		assertThat(directory.exists()).isTrue();
		assertThat(Objects.requireNonNull(directory.listFiles()).length).isEqualTo(1);
	}

	@Test
	void delete_file_shouldNotExistFile() {
		// given
		final List<Resource> resources = LocalFileUtil.readResources(Path.of(src));
		final Folder folder = Folder.builder()
			.folderType(FolderType.DRAFT)
			.path(dst)
			.build();

		// when
		localStorageService.save(resources, folder);
		List<com.donetop.domain.entity.file.File> files = new ArrayList<>(folder.getFiles());
		boolean deleteResult = localStorageService.delete(files.get(0));

		// then
		assertThat(deleteResult).isTrue();
		final File directory = Path.of(dst).toFile();
		assertThat(directory.exists()).isTrue();
		assertThat(Objects.requireNonNull(directory.listFiles()).length).isEqualTo(resources.size() - 1);
	}

}