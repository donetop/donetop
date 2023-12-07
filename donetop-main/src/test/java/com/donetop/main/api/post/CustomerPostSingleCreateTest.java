package com.donetop.main.api.post;

import com.donetop.main.api.common.CustomerPostBase;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static com.donetop.main.api.post.CustomerPostAPIController.URI.SINGULAR;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class CustomerPostSingleCreateTest extends CustomerPostBase {

	@Test
	void createSingle_withoutParts_return400() {
		// given
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"customer_post_single_create/createSingle_withoutParts_return400"
			)
		);

		// when
		final Response response = given.when()
			.post(SINGULAR);

		// then
		response.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("reason", hasSize(3));
	}

	@Test
	void createSingle_withInvalidPartValues_return400() {
		// given
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"customer_post_single_create/createSingle_withInvalidPartValues_return400"
			)
		);

		// when
		final Response response = given.when()
			.multiPart("customerName", "")
			.multiPart("title", "")
			.multiPart("content", "")
			.post(SINGULAR);

		// then
		response.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("reason", hasSize(3));
	}

	@Test
	void createSingle_withValidPartValues_return200() {
		// given
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"customer_post_single_create/createSingle_withValidPartValues_return200",
				requestParts(
					partWithName("customerName").description("The value shouldn't be empty."),
					partWithName("title").description("The value shouldn't be empty."),
					partWithName("content").description("The value shouldn't be empty.")
				),
				responseFields(
					fieldWithPath("status").type(STRING).description("Status value."),
					fieldWithPath("code").type(NUMBER).description("Status code."),
					fieldWithPath("data").type(NUMBER).description("This is the auto generated customer post id.")
				)
			)
		);

		// when
		final Response response = given.when()
			.multiPart("customerName", "jin")
			.multiPart("title", "my title")
			.multiPart("content", "my content")
			.post(SINGULAR);

		// then
		response.then()
			.statusCode(HttpStatus.OK.value())
			.body("data", greaterThanOrEqualTo(1));
	}

}
