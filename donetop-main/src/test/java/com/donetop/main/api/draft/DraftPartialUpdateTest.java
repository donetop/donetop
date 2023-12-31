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

import static com.donetop.common.api.Message.SAME_DRAFT_VALUE;
import static com.donetop.main.api.draft.DraftAPIController.URI.PARTIAL;
import static org.hamcrest.Matchers.is;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class DraftPartialUpdateTest extends DraftBase {

	@Test
	void updatePartial_withSameValuesBody_return400() throws Exception {
		// given
		final Draft draft = saveSingleDraftWithoutFiles();
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"draft_partial_update/updatePartial_withSameValuesBody_return400"
			)
		);
		final JSONObject body = new JSONObject()
			.put("draftStatus", draft.getDraftStatus())
			.put("estimateContent", draft.getEstimateContent())
			.put("memo", draft.getMemo());

		// when
		final Response response = given.when()
			.contentType(ContentType.JSON)
			.body(body.toString())
			.put(PARTIAL + "/{id}", draft.getId());

		// then
		response.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("reason", is(SAME_DRAFT_VALUE));
	}

	@Test
	void updatePartial_withEmptyBody_return200() {
		// given
		final Draft draft = saveSingleDraftWithoutFiles();
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"draft_partial_update/updatePartial_withEmptyBody_return200"
			)
		);
		final JSONObject body = new JSONObject();

		// when
		final Response response = given.when()
			.contentType(ContentType.JSON)
			.body(body.toString())
			.put(PARTIAL + "/{id}", draft.getId());

		// then
		response.then()
			.statusCode(HttpStatus.OK.value())
			.body("data", is((int) draft.getId()));
	}

	@Test
	void updatePartial_withValidBody_return200() throws Exception {
		// given
		final Draft draft = saveSingleDraftWithoutFiles();
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"draft_partial_update/updatePartial_withValidBody_return200",
				requestFields(
					fieldWithPath("draftStatus").description("The value should be one of [HOLDING, WORKING, CHECK_REQUEST, PRINT_REQUEST, COMPLETED]. If it's null, update won't be executed."),
					fieldWithPath("estimateContent").description("If the value is null, update won't be executed."),
					fieldWithPath("memo").description("If the value is null, update won't be executed.")
				),
				responseFields(
					fieldWithPath("status").type(STRING).description("Status value."),
					fieldWithPath("code").type(NUMBER).description("Status code."),
					fieldWithPath("data").type(NUMBER).description("This is the draft id.")
				)
			)
		);
		final JSONObject body = new JSONObject()
			.put("draftStatus", DraftStatus.COMPLETED)
			.put("estimateContent", "new estimateContent")
			.put("memo", "new memo");

		// when
		final Response response = given.when()
			.contentType(ContentType.JSON)
			.body(body.toString())
			.put(PARTIAL + "/{id}", draft.getId());

		// then
		response.then()
			.statusCode(HttpStatus.OK.value())
			.body("data", is((int) draft.getId()));
	}

}
