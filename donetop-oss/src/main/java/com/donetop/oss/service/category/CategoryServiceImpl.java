package com.donetop.oss.service.category;

import com.donetop.common.service.storage.Resource;
import com.donetop.common.service.storage.StorageService;
import com.donetop.domain.entity.file.File;
import com.donetop.oss.api.category.request.CategoryCreateRequest;
import com.donetop.oss.api.category.request.CategoryImageAddRequest;
import com.donetop.oss.api.category.request.CategoryImageDeleteRequest;
import com.donetop.oss.api.category.request.CategorySortRequest;
import com.donetop.domain.entity.category.Category;
import com.donetop.domain.entity.folder.Folder;
import com.donetop.dto.category.CategoryDTO;
import com.donetop.oss.properties.ApplicationProperties;
import com.donetop.oss.properties.ApplicationProperties.Storage;
import com.donetop.repository.category.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toList;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

	private final Storage storage;

	private final StorageService storageService;

	private final CategoryRepository categoryRepository;

	public CategoryServiceImpl(final ApplicationProperties applicationProperties,
							   final StorageService storageService,
							   final CategoryRepository categoryRepository) {
		this.storage = applicationProperties.getStorage();
		this.storageService = storageService;
		this.categoryRepository = categoryRepository;
	}

	@Override
	public List<CategoryDTO> categories() {
		return categoryRepository.parentCategories()
			.stream().sorted().map(Category::toDTO).collect(toList());
	}

	@Override
	public CategoryDTO getCategory(final long id) {
		return getOrThrow(id).toDTO();
	}

	@Override
	public long createNewCategory(final CategoryCreateRequest request) {
		if (request.isParentCategoryCreateRequest()) {
			final LinkedList<Category> parentCategories = categoryRepository.parentCategories().stream().sorted().collect(toCollection(LinkedList::new));
			final int sequence = parentCategories.size() == 0 ? 1 : parentCategories.getLast().getSequence() + 1;
			final Category newCategory = Category.of(request.getName(), sequence);
			return save(newCategory).getId();
		}
		final Category parentCategory = getOrThrow(request.getParentCategoryId());
		final LinkedList<Category> subCategories = parentCategory.getSubCategories().stream().sorted().collect(toCollection(LinkedList::new));
		final int sequence = subCategories.size() == 0 ? 1 : subCategories.getLast().getSequence() + 1;
		final Category newCategory = Category.of(request.getName(), sequence).setParent(parentCategory);
		return save(newCategory).getId();
	}

	@Override
	public long deleteCategory(final long id) {
		final Category category = getOrThrow(id);
		categoryRepository.delete(category);
		final List<Category> greaterSequenceCategories = category.isParent() ?
			categoryRepository.greaterSequenceParentCategories(category.getSequence()) :
			categoryRepository.greaterSequenceSubCategories(Objects.requireNonNull(category.getParent()).getId(), category.getSequence());
		greaterSequenceCategories.forEach(Category::decreaseSequence);
		return id;
	}

	@Override
	public List<CategoryDTO> sort(final CategorySortRequest request) {
		final List<CategoryDTO> categories = request.getCategories();
		categories.forEach(categoryDTO -> {
			final long id = categoryDTO.getId();
			getOrThrow(id).setSequence(categoryDTO.getSequence());
		});
		return categories;
	}

	@Override
	public long addImage(final long id, final CategoryImageAddRequest request) {
		final Category category = getOrThrow(id);
		final Folder folder = category.getOrNewFolder(storage.getRoot());
		if (folder.isNew()) {
			storageService.saveIfNotExist(folder);
			category.setFolder(folder);
		}
		return save(request.getResource(), folder).getId();
	}

	@Override
	public long deleteImage(final long categoryId, final CategoryImageDeleteRequest request) {
		final Category category = getOrThrow(categoryId);
		final long fileId = request.getFileId();
		final File file = Objects.requireNonNull(category.getFolder())
			.getFiles().stream().filter(f -> f.getId() == fileId).findFirst()
			.orElseThrow(() -> new IllegalStateException(String.format("존재하지 않는 파일입니다. id: %s", fileId)));
		storageService.delete(file);
		return fileId;
	}

	private Category getOrThrow(final long categoryId) {
		return categoryRepository.findById(categoryId)
			.orElseThrow(() -> new IllegalStateException(String.format("존재하지 않는 카테고리입니다. id: %s", categoryId)));
	}

	private File save(Resource resource, Folder folder) {
		try {
			return storageService.add(resource, folder);
		} catch (final Exception e) {
			throw new IllegalStateException("동일한 파일이 이미 존재합니다.");
		}
	}

	private Category save(final Category newCategory) {
		try {
			return categoryRepository.save(newCategory);
		} catch (final Exception e) {
			throw new IllegalStateException("동일한 이름의 카테고리가 이미 존재합니다.");
		}
	}
}
