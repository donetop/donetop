package com.donetop.main.api.draft;

import com.donetop.domain.entity.draft.Draft;
import com.donetop.domain.entity.user.User;
import com.donetop.enums.user.RoleType;
import com.donetop.main.api.common.DraftBase;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static com.donetop.common.api.Message.DISALLOWED_REQUEST;
import static com.donetop.common.api.Message.NO_SESSION;
import static com.donetop.main.api.draft.DraftAPIController.URI.COMMENT_CHECK;
import static org.hamcrest.Matchers.is;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class DraftCommentCheckTest extends DraftBase {

	@Test
	void checkComment_withoutSession_return400() {
		// given
		final Draft draft = saveSingleDraftWithComments(5);
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"draft_comment_check/checkComment_withoutSession_return400"
			)
		);

		// when
		final Response response = given.when()
			.put(COMMENT_CHECK + "/{id}", draft.getId());

		// then
		response.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("reason", is(NO_SESSION));
	}

	@Test
	void checkComment_byNormalUser_return400() throws Exception {
		// given
		final User jin = saveUser("jin", RoleType.NORMAL);
		final Draft draft = saveSingleDraftWithComments(10);
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"draft_comment_check/checkComment_byNormalUser_return400"
			)
		);

		// when
		final Response response = given.when()
			.cookies(doLoginWith(jin).cookies())
			.put(COMMENT_CHECK + "/{id}", draft.getId());

		// then
		response.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("reason", is(DISALLOWED_REQUEST));
	}

	@Test
	void checkComment_byAdmin_return200() throws Exception {
		// given
		final User admin = saveUser("admin", RoleType.ADMIN);
		final Draft draft = saveSingleDraftWithComments(10);
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"draft_comment_check/checkComment_byAdmin_return200",
				responseFields(
					fieldWithPath("status").type(STRING).description("Status value."),
					fieldWithPath("code").type(NUMBER).description("Status code."),
					fieldWithPath("data").type(NUMBER).description("This is the draft id.")
				)
			)
		);

		// when
		final Response response = given.when()
			.cookies(doLoginWith(admin).cookies())
			.put(COMMENT_CHECK + "/{id}", draft.getId());

		// then
		response.then()
			.statusCode(HttpStatus.OK.value())
			.body("data", is((int) draft.getId()));
	}

}
