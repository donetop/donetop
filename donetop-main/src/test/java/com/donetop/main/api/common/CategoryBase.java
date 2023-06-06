package com.donetop.main.api.common;

import com.donetop.common.service.storage.LocalFileUtil;
import com.donetop.common.service.storage.Resource;
import com.donetop.common.service.storage.StorageService;
import com.donetop.domain.entity.category.Category;
import com.donetop.repository.category.CategoryRepository;
import org.junit.jupiter.api.AfterAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileSystemUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

import static java.util.stream.Collectors.toCollection;

public class CategoryBase extends IntegrationBase {

	@Autowired
	protected CategoryRepository categoryRepository;

	@Autowired
	protected StorageService storageService;

	@AfterAll
	void afterAll() throws IOException {
		FileSystemUtils.deleteRecursively(Path.of(testStorage.getRoot()));
	}

	protected Category saveParentCategoryWithFiles(final String name) {
		final List<Resource> resources = LocalFileUtil.readResources(Path.of(testStorage.getSrc()));
		final LinkedList<Category> parentCategories = categoryRepository.parentCategories().stream().sorted().collect(toCollection(LinkedList::new));
		final int sequence = parentCategories.size() == 0 ? 1 : parentCategories.getLast().getSequence() + 1;
		final Category category = new Category().toBuilder()
			.name(name)
			.sequence(sequence)
			.build();
		categoryRepository.save(category);
		storageService.saveOrReplace(resources, storageService.addNewFolderOrGet(category));
		return categoryRepository.save(category);
	}

}
