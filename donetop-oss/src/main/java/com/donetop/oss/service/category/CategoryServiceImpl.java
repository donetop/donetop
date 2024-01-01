package com.donetop.oss.service.category;

import com.donetop.common.service.storage.StorageService;
import com.donetop.domain.entity.category.Category;
import com.donetop.domain.entity.file.File;
import com.donetop.domain.entity.folder.Folder;
import com.donetop.dto.category.CategoryDTO;
import com.donetop.oss.api.category.request.CategoryCreateRequest;
import com.donetop.oss.api.category.request.CategoryImageAddRequest;
import com.donetop.oss.api.category.request.CategoryImageDeleteRequest;
import com.donetop.oss.api.category.request.CategorySortRequest;
import com.donetop.repository.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static com.donetop.common.api.Message.*;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

	private final StorageService<Folder> storageService;

	private final CategoryRepository categoryRepository;

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
			final int sequence = parentCategories.isEmpty() ? 1 : parentCategories.getLast().getSequence() + 1;
			final Category newCategory = Category.of(request.getName(), sequence);
			final long id = save(newCategory).getId();
			log.info("[CREATE_PARENT] categoryId: {}", id);
			return id;
		}
		final Category parentCategory = getOrThrow(request.getParentCategoryId());
		final LinkedList<Category> subCategories = parentCategory.getSubCategories().stream().sorted().collect(toCollection(LinkedList::new));
		final int sequence = subCategories.isEmpty() ? 1 : subCategories.getLast().getSequence() + 1;
		final Category newCategory = Category.of(request.getName(), sequence).setParent(parentCategory);
		final long id = save(newCategory).getId();
		log.info("[CREATE_CHILD] parentCategoryId: {}, categoryId: {}", parentCategory.getId(), id);
		return id;
	}

	@Override
	public long deleteCategory(final long id) {
		final Category category = getOrThrow(id);
		category.getSubCategories().forEach(sub -> {
			if (sub.hasFolder()) storageService.delete(sub.getFolder());
		});
		if (category.hasFolder()) storageService.delete(category.getFolder());
		categoryRepository.delete(category);
		final List<Category> greaterSequenceCategories = category.isParent() ?
			categoryRepository.greaterSequenceParentCategories(category.getSequence()) :
			categoryRepository.greaterSequenceSubCategories(Objects.requireNonNull(category.getParent()).getId(), category.getSequence());
		greaterSequenceCategories.forEach(Category::decreaseSequence);
		log.info("[DELETE] categoryId: {}", id);
		return id;
	}

	@Override
	public List<CategoryDTO> sort(final CategorySortRequest request) {
		final List<CategoryDTO> categories = request.getCategories();
		categories.forEach(categoryDTO -> {
			final long id = categoryDTO.getId();
			getOrThrow(id).setSequence(categoryDTO.getSequence());
		});
		log.info("[SORT] categoryIds: {}", categories.stream().map(CategoryDTO::getId).collect(toList()));
		return categories;
	}

	@Override
	public void addImage(final long id, final CategoryImageAddRequest request) {
		final Category category = getOrThrow(id);
		try {
			storageService.add(request.getResources(), storageService.addNewFolderOrGet(category));
		} catch (final Exception e) {
			throw new IllegalStateException(DUPLICATED_FILE);
		}
		log.info("[ADD_IMAGE] categoryId: {}", id);
	}

	@Override
	public long deleteImage(final long categoryId, final CategoryImageDeleteRequest request) {
		final Category category = getOrThrow(categoryId);
		final long fileId = request.getFileId();
		final File file = Objects.requireNonNull(category.getFolder())
			.getFiles().stream().filter(f -> f.getId() == fileId).findFirst()
			.orElseThrow(() -> new IllegalStateException(String.format(UNKNOWN_FILE_WITH_ARGUMENTS, fileId)));
		storageService.delete(file);
		log.info("[DELETE_IMAGE] categoryId: {}", categoryId);
		return fileId;
	}

	private Category getOrThrow(final long categoryId) {
		return categoryRepository.findById(categoryId)
			.orElseThrow(() -> new IllegalStateException(String.format(UNKNOWN_CATEGORY_WITH_ARGUMENTS, categoryId)));
	}

	private Category save(final Category newCategory) {
		try {
			return categoryRepository.save(newCategory);
		} catch (final Exception e) {
			throw new IllegalStateException(DUPLICATED_CATEGORY_NAME);
		}
	}
}
