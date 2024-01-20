package com.donetop.common.service.storage;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LocalFileUtil {

	public static List<Resource> readResources(final Path path) {
		return Stream.of(Objects.requireNonNull(path.toFile().listFiles()))
			.map(LocalFileUtil::multipartFileFrom)
			.map(LocalResource::new)
			.collect(Collectors.toList());
	}

	public static List<File> readFiles(final Path path) {
		return Stream.of(Objects.requireNonNull(path.toFile().listFiles())).collect(Collectors.toList());
	}

	public static MultipartFile multipartFileFrom(final File file) {
		FileItem fileItem;
		try {
			fileItem = new DiskFileItemFactory().createItem(file.getName(),
				Files.probeContentType(file.toPath()), false, file.getName());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		try (InputStream in = new FileInputStream(file); OutputStream out = fileItem.getOutputStream()) {
			in.transferTo(out);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return new CommonsMultipartFile(fileItem);
	}

	public static void deleteAll(final Path path) {
        try {
            FileSystemUtils.deleteRecursively(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
