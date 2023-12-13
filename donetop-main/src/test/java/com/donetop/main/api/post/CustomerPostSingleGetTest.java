package com.donetop.main.api.post;

import com.donetop.domain.entity.post.CustomerPost;
import com.donetop.main.api.common.CustomerPostBase;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static com.donetop.common.api.Message.UNKNOWN_CUSTOMER_POST;
import static com.donetop.main.api.post.CustomerPostAPIController.URI.SINGULAR;
import static org.hamcrest.Matchers.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class CustomerPostSingleGetTest extends CustomerPostBase {

	@Test
	void getSingle_withInvalidId_return400() {
		// given
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"customer_post_single_get/getSingle_withInvalidId_return400"
			)
		);

		// when
		final Response response = given.when()
			.get(SINGULAR + "/{id}", 10000);

		// then
		response.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("reason", containsString(UNKNOWN_CUSTOMER_POST));
	}

	@Test
	void getSingle_withValidId_return200() {
		// given
		final CustomerPost customerPost = saveSingleCustomerPost();
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"customer_post_single_get/getSingle_withValidId_return200",
				responseFields(
					fieldWithPath("status").type(STRING).description("Status value."),
					fieldWithPath("code").type(NUMBER).description("Status code."),
					fieldWithPath("data").type(OBJECT).description("Response data."),
					fieldWithPath("data.id").type(NUMBER).description("CustomerPost id."),
					fieldWithPath("data.customerName").type(STRING).description("CustomerPost customerName."),
					fieldWithPath("data.title").type(STRING).description("CustomerPost title."),
					fieldWithPath("data.content").type(STRING).description("CustomerPost content."),
					fieldWithPath("data.createTime").type(STRING).description("CustomerPost create time."),
					subsectionWithPath("data.customerPostComments").type(ARRAY).description("CustomerPost comments."),
					fieldWithPath("data.viewCount").type(NUMBER).description("CustomerPost view count.")
				)
			)
		);

		// when
		final Response response = given.when()
			.get(SINGULAR + "/{id}", customerPost.getId());

		// then
		response.then()
			.statusCode(HttpStatus.OK.value())
			.body("data.id", is((int) customerPost.getId()))
			.body("data.customerName", is(customerPost.getCustomerName()))
			.body("data.title", is(customerPost.getTitle()))
			.body("data.content", is(customerPost.getContent()))
			.body("data.viewCount", is(1));
	}

}
