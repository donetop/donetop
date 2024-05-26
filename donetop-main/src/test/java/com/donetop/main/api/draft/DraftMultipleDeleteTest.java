package com.donetop.main.api.draft;

import com.donetop.domain.entity.draft.Draft;
import com.donetop.domain.entity.user.User;
import com.donetop.enums.user.RoleType;
import com.donetop.main.api.common.DraftBase;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static com.donetop.common.api.Message.DISALLOWED_REQUEST;
import static com.donetop.common.api.Message.NO_SESSION;
import static com.donetop.main.api.draft.DraftAPIController.URI.PLURAL;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class DraftMultipleDeleteTest extends DraftBase {

	private User jin;

	private User admin;

	@BeforeAll
	void beforeAll() {
		jin = saveUser("jin", RoleType.NORMAL);
		admin = saveUser("admin", RoleType.ADMIN);
	}

	@Test
	void deleteMultiple_withEmptyCollection_return400() throws Exception {
		// given
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"draft_multiple_delete/deleteMultiple_withEmptyCollection_return400"
			)
		);
		final JSONObject body = new JSONObject().put("draftIds", new JSONArray());

		// when
		final Response response = given.when()
			.contentType(ContentType.JSON)
			.body(body.toString())
			.put(PLURAL);

		// then
		response.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("reason", hasSize(1));
	}

	@Test
	void deleteMultiple_withoutSession_return400() throws Exception {
		// given
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"draft_multiple_delete/deleteMultiple_withoutSession_return400"
			)
		);
		final JSONObject body = new JSONObject().put("draftIds", new JSONArray().put(1L));

		// when
		final Response response = given.when()
			.contentType(ContentType.JSON)
			.body(body.toString())
			.put(PLURAL);

		// then
		response.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("reason", is(NO_SESSION));
	}

	@Test
	void deleteMultiple_byNormalUser_return400() throws Exception {
		// given
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"draft_multiple_delete/deleteMultiple_byNormalUser_return400"
			)
		);
		final JSONObject body = new JSONObject().put("draftIds", new JSONArray().put(1L));

		// when
		final Response response = given.when()
			.contentType(ContentType.JSON)
			.cookies(doLoginWith(jin).cookies())
			.body(body.toString())
			.put(PLURAL);

		// then
		response.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("reason", is(DISALLOWED_REQUEST));
	}

	@Test
	void deleteMultiple_byAdminUser_return200() throws Exception {
		// given
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"draft_multiple_delete/deleteMultiple_byAdminUser_return200",
				requestFields(
					fieldWithPath("draftIds").type(ARRAY).description("Draft ids to be deleted.")
				),
				responseFields(
					fieldWithPath("status").type(STRING).description("Status value."),
					fieldWithPath("code").type(NUMBER).description("Status code."),
					fieldWithPath("data").type(NUMBER).description("This is the number of deleted drafts.")
				)
			)
		);
		final List<Long> draftIds = saveMultipleDraftWithoutFiles(5).stream().map(Draft::getId).collect(toList());
		final JSONObject body = new JSONObject().put("draftIds", new JSONArray(draftIds));

		// when
		final Response response = given.when()
			.contentType(ContentType.JSON)
			.cookies(doLoginWith(admin).cookies())
			.body(body.toString())
			.put(PLURAL);

		// then
		response.then()
			.statusCode(HttpStatus.OK.value())
			.body("data", is(draftIds.size()));
	}

}
