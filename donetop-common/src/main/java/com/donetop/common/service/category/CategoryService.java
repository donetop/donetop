package com.donetop.common.service.category;

import com.donetop.common.api.category.CategoryCreateRequest;
import com.donetop.dto.category.CategoryDTO;

import java.util.List;

public interface CategoryService {

	List<CategoryDTO> categories();

	CategoryDTO getCategory(long id);

	long createNewCategory(CategoryCreateRequest request);

	long deleteCategory(long id);

}
