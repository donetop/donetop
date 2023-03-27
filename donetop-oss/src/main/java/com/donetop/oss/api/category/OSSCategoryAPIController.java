package com.donetop.oss.api.category;

import com.donetop.common.api.Response.OK;
import com.donetop.common.api.category.CategoryCreateRequest;
import com.donetop.common.service.category.CategoryService;
import com.donetop.dto.category.CategoryDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.donetop.oss.api.category.OSSCategoryAPIController.URI.*;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
public class OSSCategoryAPIController {

	public static class URI {
		public static final String SINGULAR = "/api/category";
		public static final String PLURAL = "/api/categories";
	}

	private final CategoryService categoryService;

	@GetMapping(value = SINGULAR + "/{id}")
	public ResponseEntity<OK<CategoryDTO>> category(@PathVariable("id") final long id) {
		return ResponseEntity.ok(OK.of(categoryService.getCategory(id)));
	}

	@DeleteMapping(value = SINGULAR + "/{id}")
	public ResponseEntity<OK<Long>> delete(@PathVariable("id") final long id) {
		return ResponseEntity.ok(OK.of(categoryService.deleteCategory(id)));
	}

	@GetMapping(value = PLURAL)
	public ResponseEntity<OK<List<CategoryDTO>>> categories() {
		return ResponseEntity.ok(OK.of(categoryService.categories()));
	}

	@PostMapping(value = SINGULAR)
	public ResponseEntity<OK<Long>> createCategory(@Valid @RequestBody final CategoryCreateRequest request) {
		return ResponseEntity.ok(OK.of(categoryService.createNewCategory(request)));
	}

}
