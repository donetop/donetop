package com.donetop.main.api.category;

import com.donetop.main.api.common.CategoryBase;
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

public class CategoryMultipleGetTest extends CategoryBase {

	@Test
	void getMultiple_category_return200() {
		// given
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"category_multiple_get/getMultiple_category_return200",
				responseFields(
					fieldWithPath("status").type(STRING).description("Status value."),
					fieldWithPath("code").type(NUMBER).description("Status code."),
					subsectionWithPath("data").type(ARRAY).description("Searched categories. See the Get Single Category response body field description for details.")
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
