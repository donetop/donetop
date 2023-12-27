package com.donetop.main.api.draft;

import com.donetop.domain.entity.draft.Draft;
import com.donetop.enums.draft.DraftStatus;
import com.donetop.main.api.common.DraftBase;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static com.donetop.common.api.Message.SAME_DRAFT_STATUS;
import static com.donetop.main.api.draft.DraftAPIController.URI.STATUS;
import static org.hamcrest.Matchers.is;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class DraftStatusUpdateTest extends DraftBase {

	@Test
	void updateStatus_withSameStatus_return400() throws Exception {
		// given
		final Draft draft = saveSingleDraftWithoutFiles();
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"draft_status_update/updateStatus_withSameStatus_return400"
			)
		);
		final JSONObject body = new JSONObject().put("draftStatus", draft.getDraftStatus());

		// when
		final Response response = given.when()
			.contentType(ContentType.JSON)
			.body(body.toString())
			.put(STATUS + "/{id}", draft.getId());

		// then
		response.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("reason", is(SAME_DRAFT_STATUS));
	}

	@Test
	void updateStatus_withDifferentStatus_return200() throws Exception {
		// given
		final Draft draft = saveSingleDraftWithoutFiles();
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"draft_status_update/updateStatus_withDifferentStatus_return200",
				requestFields(
					fieldWithPath("draftStatus").description("The value should be one of [HOLDING, WORKING, CHECK_REQUEST, PRINT_REQUEST, COMPLETED].")
				),
				responseFields(
					fieldWithPath("status").type(STRING).description("Status value."),
					fieldWithPath("code").type(NUMBER).description("Status code."),
					fieldWithPath("data").type(NUMBER).description("This is the draft id.")
				)
			)
		);
		final JSONObject body = new JSONObject().put("draftStatus", DraftStatus.COMPLETED);

		// when
		final Response response = given.when()
			.contentType(ContentType.JSON)
			.body(body.toString())
			.put(STATUS + "/{id}", draft.getId());

		// then
		response.then()
			.statusCode(HttpStatus.OK.value())
			.body("data", is((int) draft.getId()));
	}

}
