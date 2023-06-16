package com.donetop.common.service.storage;

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

import static com.donetop.common.service.storage.LocalFileUtil.readResources;
import static com.donetop.enums.folder.DomainType.CATEGORY;
import static com.donetop.enums.folder.DomainType.DRAFT;
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
	void saveOrReplace_allFiles_shouldExistAll() {
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
		assertThat(Objects.requireNonNull(Path.of(dst).toFile().listFiles()).length).isEqualTo(resources.size());
	}

	@Test
	void saveOrReplace_allFilesAndPartialFiles_shouldOnlyExistPartialFiles() {
		// given
		final List<Resource> resources = readResources(Path.of(src));
		final Folder folder = Folder.builder()
			.domainType(DRAFT)
			.path(dst)
			.build();

		// when
		localStorageService.saveOrReplace(resources, folder);
		localStorageService.saveOrReplace(resources.subList(0, resources.size() / 2), folder);

		// then
		assertThat(folder.getFiles().size()).isEqualTo(resources.size() / 2);
		assertThat(Objects.requireNonNull(Path.of(dst).toFile().listFiles()).length).isEqualTo(resources.size() / 2);
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
		assertThat(Objects.requireNonNull(Path.of(dst).toFile().listFiles()).length).isEqualTo(resources.size());
	}

	@Test
	void add_filesMultipleTimes_shouldExistAll() {
		// given
		final List<List<Resource>> resourcesList = readResources(Path.of(src))
			.stream().map(List::of).collect(Collectors.toList());
		final Folder folder = Folder.builder()
			.domainType(DRAFT)
			.path(dst)
			.build();

		// when
		resourcesList.forEach(resources -> localStorageService.add(resources, folder));

		// then
		assertThat(folder.getFiles().size()).isEqualTo(resourcesList.size());
		assertThat(Objects.requireNonNull(Path.of(dst).toFile().listFiles()).length).isEqualTo(resourcesList.size());
	}

	@Test
	void addNewFolderOrGet_withSingleFolderContainer_shouldExistFolder() {
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
	void delete_allFiles_shouldExistFolderAndNotExistFiles() {
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