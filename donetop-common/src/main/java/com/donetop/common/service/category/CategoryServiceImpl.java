package com.donetop.common.service.category;

import com.donetop.common.api.category.CategoryCreateRequest;
import com.donetop.common.api.category.CategorySortRequest;
import com.donetop.domain.entity.category.Category;
import com.donetop.dto.category.CategoryDTO;
import com.donetop.repository.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toList;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

	private final CategoryRepository categoryRepository;

	private final String UNKNOWN_CATEGORY_MESSAGE = "존재하지 않는 카테고리입니다. id: %s";

	@Override
	public List<CategoryDTO> categories() {
		return categoryRepository.parentCategories()
			.stream().sorted().map(Category::toDTO).collect(toList());
	}

	@Override
	public CategoryDTO getCategory(final long id) {
		return categoryRepository.findById(id)
			.orElseThrow(() -> new IllegalStateException(String.format(UNKNOWN_CATEGORY_MESSAGE, id)))
			.toDTO();
	}

	@Override
	public long createNewCategory(final CategoryCreateRequest request) {
		if (request.isParentCategoryCreateRequest()) {
			final LinkedList<Category> parentCategories = categoryRepository.parentCategories().stream().sorted().collect(toCollection(LinkedList::new));
			final int sequence = parentCategories.size() == 0 ? 1 : parentCategories.getLast().getSequence() + 1;
			final Category newCategory = Category.of(request.getName(), sequence);
			return save(newCategory).getId();
		}
		final Category parentCategory = categoryRepository.findById(request.getParentCategoryId())
			.orElseThrow(() -> new IllegalStateException(String.format(UNKNOWN_CATEGORY_MESSAGE, request.getParentCategoryId())));
		final LinkedList<Category> subCategories = parentCategory.getSubCategories().stream().sorted().collect(toCollection(LinkedList::new));
		final int sequence = subCategories.size() == 0 ? 1 : subCategories.getLast().getSequence() + 1;
		final Category newCategory = Category.of(request.getName(), sequence).setParent(parentCategory);
		return save(newCategory).getId();
	}

	@Override
	public long deleteCategory(final long id) {
		final Category category = categoryRepository.findById(id)
			.orElseThrow(() -> new IllegalStateException(String.format(UNKNOWN_CATEGORY_MESSAGE, id)));
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
			categoryRepository.findById(id)
				.orElseThrow(() -> new IllegalStateException(String.format(UNKNOWN_CATEGORY_MESSAGE, id)))
				.setSequence(categoryDTO.getSequence());
		});
		return categories;
	}

	private Category save(Category newCategory) {
		try {
			return categoryRepository.save(newCategory);
		} catch (final Exception e) {
			throw new IllegalStateException("동일한 이름의 카테고리가 이미 존재합니다.");
		}
	}
}
