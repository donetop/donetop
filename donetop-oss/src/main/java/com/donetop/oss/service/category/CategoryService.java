package com.donetop.oss.service.category;

import com.donetop.oss.api.category.request.CategoryCreateRequest;
import com.donetop.oss.api.category.request.CategoryImageAddRequest;
import com.donetop.oss.api.category.request.CategoryImageDeleteRequest;
import com.donetop.oss.api.category.request.CategorySortRequest;
import com.donetop.dto.category.CategoryDTO;

import java.util.List;

public interface CategoryService {

	List<CategoryDTO> categories();

	CategoryDTO getCategory(long id);

	long createNewCategory(CategoryCreateRequest request);

	long deleteCategory(long id);

	List<CategoryDTO> sort(CategorySortRequest request);

	void addImage(long id, CategoryImageAddRequest request);

	long deleteImage(long categoryId, CategoryImageDeleteRequest request);

}
