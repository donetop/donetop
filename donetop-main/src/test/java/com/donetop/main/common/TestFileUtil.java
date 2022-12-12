package com.donetop.main.common;

import com.donetop.main.service.storage.LocalResource;
import com.donetop.main.service.storage.Resource;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TestFileUtil {

	public static List<Resource> readResources(final Path path) {
		return Arrays.stream(Objects.requireNonNull(path.toFile().listFiles()))
			.map(TestFileUtil::multipartFileFrom)
			.map(LocalResource::new)
			.collect(Collectors.toList());
	}

	public static List<MockMultipartFile> readMultipartFiles(final Path path) {
		return Arrays.stream(Objects.requireNonNull(path.toFile().listFiles()))
			.map(TestFileUtil::multipartFileFrom)
			.collect(Collectors.toList());
	}

	private static MockMultipartFile multipartFileFrom(final File file) {
		final String fileName = file.getName();
		final String mimeType = URLConnection.guessContentTypeFromName(fileName);
		try (InputStream inputStream = new FileInputStream(file)) {
			return new MockMultipartFile("files", fileName, mimeType, inputStream);
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

}
