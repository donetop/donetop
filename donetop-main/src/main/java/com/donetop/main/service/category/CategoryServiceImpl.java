package com.donetop.main.service.category;

import com.donetop.domain.entity.category.Category;
import com.donetop.dto.category.CategoryDTO;
import com.donetop.repository.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

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

	private Category getOrThrow(final long categoryId) {
		return categoryRepository.findById(categoryId)
			.orElseThrow(() -> new IllegalStateException(String.format("존재하지 않는 카테고리입니다. id: %s", categoryId)));
	}

}
