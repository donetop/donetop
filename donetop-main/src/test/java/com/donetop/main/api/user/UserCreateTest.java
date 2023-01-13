package com.donetop.main.api.user;

import com.donetop.main.api.common.IntegrationBase;
import com.donetop.repository.user.UserRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import static com.donetop.main.api.user.UserAPIController.Uri.*;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.*;

public class UserCreateTest extends IntegrationBase {

	@Autowired
	private UserRepository userRepository;

	@AfterAll
	void afterAll() {
		userRepository.deleteAll();
	}

	@Test
	void create_withNullAndEmptyValues_return400() throws Exception {
	    // given
		final JSONObject body = new JSONObject();
		body.put("email", "");
		body.put("name", JSONObject.NULL);
		body.put("password", "");
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"user_create/create_withNullAndEmptyValues_return400"
			)
		);

		// when
		final Response response = given.when()
			.contentType(ContentType.JSON)
			.body(body.toString())
			.post(SINGULAR);

		// then
		response.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("reason", hasSize(3))
		;
	}

	@Test
	void create_withValidValues_return200() throws Exception {
		// given
		final JSONObject body = new JSONObject();
		body.put("email", "jin@test.com");
		body.put("name", "jin");
		body.put("password", "my password");
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"user_create/create_withValidValues_return200",
				requestFields(
					fieldWithPath("email").type(STRING).description("This parameter shouldn't be empty."),
					fieldWithPath("name").type(STRING).description("This parameter shouldn't be empty."),
					fieldWithPath("password").type(STRING).description("This parameter shouldn't be empty.")
				),
				responseFields(
					fieldWithPath("status").type(STRING).description("Status value."),
					fieldWithPath("code").type(NUMBER).description("Status code."),
					fieldWithPath("data").type(NUMBER).description("This is auto generated user id.")
				)
			)
		);

		// when
		final Response response = given.when()
			.contentType(ContentType.JSON)
			.body(body.toString())
			.post(SINGULAR);

		// then
		response.then()
			.statusCode(HttpStatus.OK.value())
			.body("data", greaterThan(0))
		;
	}

}
