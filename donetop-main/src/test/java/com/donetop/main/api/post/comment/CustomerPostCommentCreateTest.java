package com.donetop.main.api.post.comment;

import com.donetop.domain.entity.post.CustomerPost;
import com.donetop.main.api.common.CustomerPostCommentBase;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static com.donetop.main.api.post.CustomerPostCommentAPIController.URI.SINGULAR;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class CustomerPostCommentCreateTest extends CustomerPostCommentBase {

	@Test
	void create_withoutParts_return400() {
		// given
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"customer_post_comment_create/create_withoutParts_return400"
			)
		);

		// when
		final Response response = given.when()
			.post(SINGULAR);

		// then
		response.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("reason", hasSize(2));
	}

	@Test
	void create_withInvalidPartValues_return400() {
		// given
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"customer_post_comment_create/create_withInvalidPartValues_return400"
			)
		);

		// when
		final Response response = given.when()
			.multiPart("content", "")
			.multiPart("customerPostId", -1)
			.post(SINGULAR);

		// then
		response.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("reason", hasSize(2));
	}

	@Test
	void create_withValidPartValues_return200() {
		// given
		final CustomerPost customerPost = saveSingleCustomerPost();
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"customer_post_comment_create/create_withValidPartValues_return200",
				requestParts(
					partWithName("content").description("The value shouldn't be empty."),
					partWithName("customerPostId").description("The value should be greater than 0.")
				),
				responseFields(
					fieldWithPath("status").type(STRING).description("Status value."),
					fieldWithPath("code").type(NUMBER).description("Status code."),
					fieldWithPath("data").type(NUMBER).description("This is the auto generated comment id.")
				)
			)
		);

		// when
		final Response response = given.when()
			.multiPart("content", "my content")
			.multiPart("customerPostId", customerPost.getId())
			.post(SINGULAR);

		// then
		response.then()
			.statusCode(HttpStatus.OK.value())
			.body("data", greaterThanOrEqualTo(1));
	}

}
