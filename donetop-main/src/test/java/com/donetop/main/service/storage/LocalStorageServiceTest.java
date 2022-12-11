package com.donetop.main.service.storage;

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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.FileSystemUtils;

import java.io.*;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
@TestPropertySource(
	properties = {
		"src=src/test/resources/storage/file",
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
	void read_filesFromSRC_shouldExist() {
		// given

		// when
		final File file = Path.of(src).toFile();

		// then
		assertThat(file.exists()).isTrue();
		assertThat(file.isDirectory()).isTrue();
		assertThat(Objects.requireNonNull(file.listFiles()).length).isEqualTo(3);
	}

	@Test
	void save_filesAtDST_shouldExist() {
		// given
		final List<Resource> resources = readResources();
		final Folder folder = Folder.builder()
			.folderType(FolderType.DRAFT)
			.path(dst)
			.build();

		// when
		localStorageService.save(resources, folder);

		// then
		assertThat(Objects.requireNonNull(Path.of(dst).toFile().listFiles()).length).isEqualTo(3);
	}

	@Test
	void delete_file_shouldBeRemoved() {
		// given
		final List<Resource> resources = readResources();
		final Folder folder = Folder.builder()
			.folderType(FolderType.DRAFT)
			.path(dst)
			.build();
		given(fileRepository.saveAll(anyCollection())).will(returnFirstParameter);

		// when
		final List<com.donetop.domain.entity.file.File> files = localStorageService.save(resources, folder);
		boolean deleteResult = localStorageService.delete(files.get(0));

		// then
		assertThat(deleteResult).isTrue();
		assertThat(Objects.requireNonNull(Path.of(dst).toFile().listFiles()).length).isEqualTo(2);
	}

	@Test
	void delete_folder_shouldBeRemoved() {
		// given
		final List<Resource> resources = readResources();
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

	private List<Resource> readResources() {
		return Arrays.stream(Objects.requireNonNull(Path.of(src).toFile().listFiles()))
			.map(file -> {
				final String fileName = file.getName();
				final String originalFileName = fileName.substring(0, fileName.lastIndexOf("."));
				final String mimeType = URLConnection.guessContentTypeFromName(fileName);
				try (InputStream inputStream = new FileInputStream(file)) {
					return new MockMultipartFile(fileName, originalFileName, mimeType, inputStream);
				} catch (final Exception e) {
					throw new RuntimeException(e);
				}
			})
			.map(LocalResource::new)
			.collect(Collectors.toList());
	}

}