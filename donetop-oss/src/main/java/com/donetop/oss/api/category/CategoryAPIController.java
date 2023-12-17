package com.donetop.oss.api.category;

import com.donetop.common.api.Response.OK;
import com.donetop.oss.api.category.request.CategoryCreateRequest;
import com.donetop.oss.api.category.request.CategoryImageAddRequest;
import com.donetop.oss.api.category.request.CategoryImageDeleteRequest;
import com.donetop.oss.api.category.request.CategorySortRequest;
import com.donetop.oss.service.category.CategoryService;
import com.donetop.dto.category.CategoryDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.donetop.oss.api.category.CategoryAPIController.URI.*;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
public class CategoryAPIController {

	public static class URI {
		public static final String SINGULAR = "/api/category";
		public static final String PLURAL = "/api/categories";
		public static final String IMAGE = SINGULAR + "/image";
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

	@PostMapping(value = IMAGE + "/{id}")
	public ResponseEntity<OK<Long>> addImage(@PathVariable("id") final long id, @Valid final CategoryImageAddRequest request) {
		categoryService.addImage(id, request);
		return ResponseEntity.ok(OK.of(id));
	}

	@PutMapping(value = IMAGE + "/{id}")
	public ResponseEntity<OK<Long>> deleteImage(@PathVariable("id") final long id, @Valid @RequestBody final CategoryImageDeleteRequest request) {
		return ResponseEntity.ok(OK.of(categoryService.deleteImage(id, request)));
	}

	@GetMapping(value = PLURAL)
	public ResponseEntity<OK<List<CategoryDTO>>> categories() {
		return ResponseEntity.ok(OK.of(categoryService.categories()));
	}

	@PostMapping(value = SINGULAR)
	public ResponseEntity<OK<Long>> createCategory(@Valid @RequestBody final CategoryCreateRequest request) {
		return ResponseEntity.ok(OK.of(categoryService.createNewCategory(request)));
	}

	@PutMapping(value = PLURAL + "/sort")
	public ResponseEntity<OK<List<CategoryDTO>>> sort(@Valid @RequestBody final CategorySortRequest request) {
		return ResponseEntity.ok(OK.of(categoryService.sort(request)));
	}

}
