package com.donetop.main.api.category;

import com.donetop.dto.category.CategoryDTO;
import com.donetop.common.api.Response.OK;
import com.donetop.main.service.category.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.donetop.main.api.category.CategoryAPIController.URI.*;

@RestController
@RequiredArgsConstructor
public class CategoryAPIController {

	public static class URI {
		public static final String SINGULAR = "/api/category";
		public static final String PLURAL = "/api/categories";
	}

	private final CategoryService categoryService;

	@GetMapping(value = SINGULAR + "/{id}")
	public ResponseEntity<OK<CategoryDTO>> category(@PathVariable("id") final long id) {
		return ResponseEntity.ok(OK.of(categoryService.getCategory(id)));
	}

	@GetMapping(value = PLURAL)
	public ResponseEntity<OK<List<CategoryDTO>>> categories() {
		return ResponseEntity.ok(OK.of(categoryService.categories()));
	}

}
