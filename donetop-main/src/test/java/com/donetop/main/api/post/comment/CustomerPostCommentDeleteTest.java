package com.donetop.main.api.post.comment;

import com.donetop.domain.entity.post.CustomerPostComment;
import com.donetop.domain.entity.user.User;
import com.donetop.enums.user.RoleType;
import com.donetop.main.api.common.CustomerPostCommentBase;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static com.donetop.common.api.Message.*;
import static com.donetop.main.api.post.CustomerPostCommentAPIController.URI.SINGULAR;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class CustomerPostCommentDeleteTest extends CustomerPostCommentBase {

	private User jin;

	private User admin;

	@BeforeAll
	void beforeAll() {
		jin = saveUser("jin", RoleType.NORMAL);
		admin = saveUser("admin", RoleType.ADMIN);
	}

	@Test
	void delete_withoutSession_return400() {
		// given
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"customer_post_comment_delete/delete_withoutSession_return400"
			)
		);

		// when
		final Response response = given.when()
			.delete(SINGULAR + "/{id}", -1);

		// then
		response.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("reason", is(NO_SESSION));
	}

	@Test
	void delete_withUnknownId_return400() throws Exception {
		// given
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"customer_post_comment_delete/delete_withUnknownId_return400"
			)
		);

		// when
		final Response response = given.when()
			.cookies(doLoginWith(admin).cookies())
			.delete(SINGULAR + "/{id}", -1);

		// then
		response.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("reason", containsString(UNKNOWN_COMMENT));
	}

	@Test
	void delete_byNormalUser_return400() throws Exception {
		// given
		final CustomerPostComment customerPostComment = saveSingleCustomerPostComment();
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"customer_post_comment_delete/delete_byNormalUser_return400"
			)
		);

		// when
		final Response response = given.when()
			.cookies(doLoginWith(jin).cookies())
			.delete(SINGULAR + "/{id}", customerPostComment.getId());

		// then
		response.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("reason", is(DISALLOWED_REQUEST));
	}

	@Test
	void delete_byAdminUser_return200() throws Exception {
		// given
		final CustomerPostComment customerPostComment = saveSingleCustomerPostComment();
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"customer_post_comment_delete/delete_byAdminUser_return200",
				responseFields(
					fieldWithPath("status").type(STRING).description("Status value."),
					fieldWithPath("code").type(NUMBER).description("Status code."),
					fieldWithPath("data").type(NUMBER).description("This is comment id.")
				)
			)
		);

		// when
		final Response response = given.when()
			.cookies(doLoginWith(admin).cookies())
			.delete(SINGULAR + "/{id}", customerPostComment.getId());

		// then
		response.then()
			.statusCode(HttpStatus.OK.value())
			.body("data", is((int) customerPostComment.getId()));
	}

}
