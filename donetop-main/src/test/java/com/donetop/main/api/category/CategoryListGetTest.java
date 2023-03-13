package com.donetop.main.api.category;

import com.donetop.main.api.common.IntegrationBase;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static com.donetop.main.api.category.CategoryAPIController.URI.PLURAL;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class CategoryListGetTest extends IntegrationBase {

	@Test
	void getList_category_return200() {
		// given
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"category_list_get/getList_category_return200",
				responseFields(
					fieldWithPath("status").type(STRING).description("Status value."),
					fieldWithPath("code").type(NUMBER).description("Status code."),
					fieldWithPath("data").type(ARRAY).description("Response data."),
					fieldWithPath("data[].id").type(NUMBER).description("Category id."),
					fieldWithPath("data[].name").type(STRING).description("Category name."),
					fieldWithPath("data[].sequence").type(NUMBER).description("Category sequence."),
					subsectionWithPath("data[].subCategories").type(ARRAY).description("Sub category list.")
				)
			)
		);

		// when
		final Response response = given.when().get(PLURAL);

		// then
		response.then()
			.statusCode(HttpStatus.OK.value())
			.body("data", hasSize(10))
		;
	}

}
