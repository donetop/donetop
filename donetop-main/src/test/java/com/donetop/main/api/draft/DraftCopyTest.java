package com.donetop.main.api.draft;

import com.donetop.domain.entity.draft.Draft;
import com.donetop.main.api.common.DraftBase;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static com.donetop.main.api.draft.DraftAPIController.URI.COPY;
import static org.hamcrest.Matchers.*;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class DraftCopyTest extends DraftBase {

	@Test
	void copy_withInvalidId_return400() throws Exception {
		// given
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"draft_copy/copy_withInvalidId_return400"
			)
		);
		JSONObject body = new JSONObject().put("id", -1);

		// when
		final Response response = given.when()
			.contentType(ContentType.JSON)
			.body(body.toString())
			.post(COPY);

		// then
		response.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("reason", hasSize(1));
	}

	@Test
	void copy_withUnknownId_return400() throws Exception {
		// given
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"draft_copy/copy_withUnknownId_return400"
			)
		);
		JSONObject body = new JSONObject().put("id", 2000);

		// when
		final Response response = given.when()
			.contentType(ContentType.JSON)
			.body(body.toString())
			.post(COPY);

		// then
		response.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("reason", containsString("존재하지 않는 시안입니다."));
	}

	@Test
	void copy_noFolderDraft_return200() throws Exception {
		// given
		final Draft draft = saveSingleDraftWithoutFiles();
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"draft_copy/copy_noFolderDraft_return200"
			)
		);
		JSONObject body = new JSONObject().put("id", draft.getId());

		// when
		final Response response = given.when()
			.contentType(ContentType.JSON)
			.body(body.toString())
			.post(COPY);

		// then
		response.then()
			.statusCode(HttpStatus.OK.value())
			.body("data", not(draft.getId()));
	}

	@Test
	void copy_hasFolderDraft_return200() throws Exception {
		// given
		final Draft draft = saveSingleDraftWithFiles();
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"draft_copy/copy_hasFolderDraft_return200",
				requestFields(
					fieldWithPath("id").description("The draft id for copying.")
				),
				responseFields(
					fieldWithPath("status").type(STRING).description("Status value."),
					fieldWithPath("code").type(NUMBER).description("Status code."),
					fieldWithPath("data").type(NUMBER).description("This is the copied draft id.")
				)
			)
		);
		JSONObject body = new JSONObject().put("id", draft.getId());

		// when
		final Response response = given.when()
			.contentType(ContentType.JSON)
			.body(body.toString())
			.post(COPY);

		// then
		response.then()
			.statusCode(HttpStatus.OK.value())
			.body("data", not(draft.getId()));
	}

}
