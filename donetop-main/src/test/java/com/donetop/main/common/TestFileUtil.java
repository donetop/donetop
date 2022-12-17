package com.donetop.main.common;

import com.donetop.main.service.storage.LocalResource;
import com.donetop.main.service.storage.Resource;
import org.apache.tika.Tika;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestFileUtil {

	private static final Tika tika = new Tika();

	public static List<Resource> readResources(final Path path) {
		return Stream.of(Objects.requireNonNull(path.toFile().listFiles()))
			.map(TestFileUtil::multipartFileFrom)
			.map(LocalResource::new)
			.collect(Collectors.toList());
	}

	public static List<MockMultipartFile> readMultipartFiles(final Path path) {
		return Stream.of(Objects.requireNonNull(path.toFile().listFiles()))
			.map(TestFileUtil::multipartFileFrom)
			.collect(Collectors.toList());
	}

	public static List<File> readFiles(final Path path) {
		return Stream.of(Objects.requireNonNull(path.toFile().listFiles())).collect(Collectors.toList());
	}

	private static MockMultipartFile multipartFileFrom(final File file) {
		final String fileName = file.getName();
		final String mimeType = tika.detect(fileName);
		try (InputStream inputStream = new FileInputStream(file)) {
			return new MockMultipartFile("files", fileName, mimeType, inputStream);
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

}
