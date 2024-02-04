package com.donetop.common.service.storage;

import com.donetop.common.service.storage.Resource.FileSaveInfo;
import com.donetop.domain.entity.category.Category;
import com.donetop.domain.entity.folder.Folder;
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

import static com.donetop.common.service.storage.LocalFileUtil.*;
import static com.donetop.enums.folder.DomainType.CATEGORY;
import static com.donetop.enums.folder.DomainType.DRAFT;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
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

	@Mock
	private Storage storage;

	@InjectMocks
	private LocalStorageService<Folder> localStorageService;

	final Answer<Object> returnFirstArgument = i -> i.getArguments()[0];

	@Value("${src}")
	private String src;

	@Value("${dst}")
	private String dst;

	@BeforeEach
	void beforeEach() throws IOException {
		Files.createDirectories(Path.of(dst));
	}

	@AfterEach
	void afterEach() throws IOException {
		FileSystemUtils.deleteRecursively(Path.of(dst));
	}

	@Test
	void saveOrReplace_files_shouldExistAll() {
		// given
		final List<Resource> resources = readResources(Path.of(src));
		final Folder folder = Folder.builder()
			.domainType(DRAFT)
			.path(dst)
			.build();

		// when
		localStorageService.saveOrReplace(resources, folder);

		// then
		assertThat(folder.getFiles().size()).isEqualTo(resources.size());
		assertThat(numberOfFiles(Path.of(dst))).isEqualTo(resources.size());
	}

	@Test
	void saveOrReplace_allFilesFirstAndThenPartialFiles_shouldOnlyExistPartialFiles() {
		// given
		final List<Resource> resources = readResources(Path.of(src));
		final int partialSize = resources.size() / 2;
		final Folder folder = Folder.builder()
			.domainType(DRAFT)
			.path(dst)
			.build();

		// when
		localStorageService.saveOrReplace(resources, folder);
		localStorageService.saveOrReplace(resources.subList(0, partialSize), folder);

		// then
		assertThat(folder.getFiles().size()).isEqualTo(partialSize);
		assertThat(numberOfFiles(Path.of(dst))).isEqualTo(partialSize);
	}

	@Test
	void add_allFiles_shouldExistAll() {
		// given
		final List<Resource> resources = readResources(Path.of(src));
		final Folder folder = Folder.builder()
			.domainType(DRAFT)
			.path(dst)
			.build();

		// when
		localStorageService.add(resources, folder);

		// then
		assertThat(folder.getFiles().size()).isEqualTo(resources.size());
		assertThat(numberOfFiles(Path.of(dst))).isEqualTo(resources.size());
	}

	@Test
	void addMultipleTimes_eachFile_shouldExistAll() {
		// given
		final List<List<Resource>> resourcesList = readResources(Path.of(src))
			.stream().map(List::of).collect(toList());
		final Folder folder = Folder.builder()
			.domainType(DRAFT)
			.path(dst)
			.build();

		// when
		resourcesList.forEach(resources -> localStorageService.add(resources, folder));

		// then
		assertThat(folder.getFiles().size()).isEqualTo(resourcesList.size());
		assertThat(numberOfFiles(Path.of(dst))).isEqualTo(resourcesList.size());
	}

	@Test
	void add_normalFilesAndThenExceptionalFiles_shouldNotExistExceptionFiles() {
		// given
		final Folder folder = Folder.builder()
			.domainType(DRAFT)
			.path(dst)
			.build();
		final List<String> saveSuccessFileNames = localStorageService.add(readResources(Path.of(src)).subList(0, 2), folder)
			.stream().map(com.donetop.domain.entity.file.File::getName).collect(toList());
		final List<Resource> mockResources = readMockResources(Path.of(src)).subList(2, 4);
		final List<com.donetop.domain.entity.file.File> saveSuccessFiles = mockResources.stream()
			.map(resource -> resource.saveAt(folder))
			.filter(FileSaveInfo::isSuccess)
			.map(FileSaveInfo::getFile).collect(Collectors.toList());
		given(fileRepository.saveAll(saveSuccessFiles)).willThrow(new IllegalStateException("Can't save resources"));

		// when
		try {
			localStorageService.add(mockResources, folder);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		// then
		assertThat(Path.of(folder.getPath()).toFile().exists()).isTrue();
		readFiles(Path.of(folder.getPath())).forEach(file -> assertThat(saveSuccessFileNames.contains(file.getName())).isTrue());
		assertThat(numberOfFiles(Path.of(dst))).isEqualTo(saveSuccessFileNames.size());
	}

	@Test
	void addNewFolderOrGet_atSingleFolderContainer_shouldExistFolder() {
		// given
		final Category category = Category.builder().id(1L).build();
		given(storage.getRoot()).willReturn(dst);
		given(folderRepository.save(any())).will(returnFirstArgument);

		// when
		Folder folder = localStorageService.addNewFolderOrGet(category);

		// then
		assertThat(folder).isEqualTo(category.getFolder());
		final String folderPath = CATEGORY.buildPathFrom(dst, category.getId());
		assertThat(Path.of(folderPath).toFile().exists()).isTrue();
	}

	@Test
	void deleteAllFiles_inTheFolder_shouldExistFolderButNotExistFiles() {
		// given
		final List<Resource> resources = readResources(Path.of(src));
		final Folder folder = Folder.builder()
			.domainType(DRAFT)
			.path(dst)
			.build();
		given(fileRepository.saveAll(anyCollection())).will(returnFirstArgument);

		// when
		localStorageService.add(resources, folder);
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
		final List<Resource> resources = readResources(Path.of(src));
		final Folder folder = Folder.builder()
			.domainType(DRAFT)
			.path(dst)
			.build();

		// when
		localStorageService.add(resources, folder);
		boolean deleteResult = localStorageService.delete(folder);

		// then
		assertThat(deleteResult).isTrue();
		assertThat(Path.of(dst).toFile().exists()).isFalse();
	}

	@Test
	void delete_file_shouldNotExistFile() {
		// given
		final List<Resource> resources = readResources(Path.of(src));
		final Folder folder = Folder.builder()
			.domainType(DRAFT)
			.path(dst)
			.build();

		// when
		localStorageService.add(resources, folder);
		List<com.donetop.domain.entity.file.File> files = new ArrayList<>(folder.getFiles());
		boolean deleteResult = localStorageService.delete(files.get(0));

		// then
		assertThat(deleteResult).isTrue();
		final File directory = Path.of(dst).toFile();
		assertThat(directory.exists()).isTrue();
		assertThat(Objects.requireNonNull(directory.listFiles()).length).isEqualTo(resources.size() - 1);
	}

}