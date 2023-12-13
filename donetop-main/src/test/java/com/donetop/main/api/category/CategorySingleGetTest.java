package com.donetop.main.api.category;

import com.donetop.domain.entity.category.Category;
import com.donetop.main.api.common.CategoryBase;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static com.donetop.common.api.Message.UNKNOWN_CATEGORY;
import static com.donetop.main.api.category.CategoryAPIController.URI.SINGULAR;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class CategorySingleGetTest extends CategoryBase {

	@Test
	void getSingle_withInvalidId_return400() {
		// given
		final long id = 10000;
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"category_single_get/getSingle_withInvalidId_return400"
			)
		);

		// when
		final Response response = given.when()
			.get(SINGULAR + "/{id}", id);

		// then
		response.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("reason", containsString(UNKNOWN_CATEGORY));
	}

	@Test
	void getSingle_withValidId_return200() {
		// given
		final Category category = saveParentCategoryWithFiles("test category");
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"category_single_get/getSingle_withValidId_return200",
				responseFields(
					fieldWithPath("status").type(STRING).description("Status value."),
					fieldWithPath("code").type(NUMBER).description("Status code."),
					fieldWithPath("data.id").type(NUMBER).description("Category id."),
					fieldWithPath("data.name").type(STRING).description("Category name."),
					fieldWithPath("data.sequence").type(NUMBER).description("Category sequence."),
					subsectionWithPath("data.folder").type(OBJECT).description("Category's folder."),
					subsectionWithPath("data.subCategories").type(ARRAY).description("Sub category list.")
				)
			)
		);

		// when
		final Response response = given.when()
			.get(SINGULAR + "/{id}", category.getId());

		// then
		response.then()
			.statusCode(HttpStatus.OK.value())
			.body("data.id", is(Integer.valueOf(String.valueOf(category.getId()))))
			.body("data.name", is(category.getName()))
			.body("data.sequence", is(category.getSequence()));
	}

}
