package com.donetop.main.api.authentication.form;

import com.donetop.domain.entity.user.User;
import com.donetop.main.api.common.IntegrationBase;
import com.donetop.repository.user.UserRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.restassured3.RestAssuredRestDocumentation;

import static com.donetop.main.api.authentication.form.FormAPIController.Uri.*;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

public class FormAuthenticationTest extends IntegrationBase {

	@Autowired
	private UserRepository userRepository;

	@BeforeAll
	void beforeAll() {
		User user = User.builder()
			.email("jin@test.com")
			.name("jin")
			.password("my password")
			.build();
		userRepository.save(user);
	}

	@AfterAll
	void afterAll() {
		userRepository.deleteAll();
	}

	@Test
	void authentication_withEmptyBody_return400() {
		// given
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			RestAssuredRestDocumentation.document(
				"form_authentication/authentication_withEmptyBody_return400"
			)
		);

		// when
		final Response response = given.when()
			.contentType(ContentType.JSON)
			.post(AUTHENTICATION);

		// then
		response.then()
			.statusCode(HttpStatus.BAD_REQUEST.value());
	}

	@Test
	void authentication_withIncorrectFields_return400() throws Exception {
		// given
		final JSONObject body = new JSONObject();
		body.put("usernamee", "jin");
		body.put("passworddd", "password");
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			RestAssuredRestDocumentation.document(
				"form_authentication/authentication_withIncorrectFields_return400"
			)
		);

		// when
		final Response response = given.when()
			.contentType(ContentType.JSON)
			.body(body.toString())
			.post(AUTHENTICATION);

		// then
		response.then()
			.statusCode(HttpStatus.BAD_REQUEST.value());
	}

	@Test
	void authentication_withNullValues_return400() throws Exception {
		// given
		final JSONObject body = new JSONObject();
		body.put("username", JSONObject.NULL);
		body.put("password", JSONObject.NULL);
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			RestAssuredRestDocumentation.document(
				"form_authentication/authentication_withNullValues_return400"
			)
		);

		// when
		final Response response = given.when()
			.contentType(ContentType.JSON)
			.body(body.toString())
			.post(AUTHENTICATION);

		// then
		response.then()
			.statusCode(HttpStatus.BAD_REQUEST.value());
	}

	@Test
	void authentication_withEmptyValues_return400() throws Exception {
		// given
		final JSONObject body = new JSONObject();
		body.put("username", "");
		body.put("password", "");
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			RestAssuredRestDocumentation.document(
				"form_authentication/authentication_withEmptyValues_return400"
			)
		);

		// when
		final Response response = given.when()
			.contentType(ContentType.JSON)
			.body(body.toString())
			.post(AUTHENTICATION);

		// then
		response.then()
			.statusCode(HttpStatus.BAD_REQUEST.value());
	}

	@Test
	void authentication_withWrongUsername_return400() throws Exception {
		// given
		final JSONObject body = new JSONObject();
		body.put("username", "unknown");
		body.put("password", "unknown");
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			RestAssuredRestDocumentation.document(
				"form_authentication/authentication_withWrongUsername_return400"
			)
		);

		// when
		final Response response = given.when()
			.contentType(ContentType.JSON)
			.body(body.toString())
			.post(AUTHENTICATION);

		// then
		response.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("reason", containsString("유저이름이 유효하지 않습니다."));
	}

	@Test
	void authentication_withWrongPassword_return400() throws Exception {
		// given
		final JSONObject body = new JSONObject();
		body.put("username", "jin");
		body.put("password", "unknown");
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			RestAssuredRestDocumentation.document(
				"form_authentication/authentication_withWrongPassword_return400"
			)
		);

		// when
		final Response response = given.when()
			.contentType(ContentType.JSON)
			.body(body.toString())
			.post(AUTHENTICATION);

		// then
		response.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("reason", containsString("비밀번호가 유효하지 않습니다."));
	}

	@Test
	void authentication_withValidInfo_return200() throws Exception {
		// given
		final JSONObject body = new JSONObject();
		body.put("username", "jin");
		body.put("password", "my password");
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			RestAssuredRestDocumentation.document(
				"form_authentication/authentication_withValidInfo_return200",
				requestFields(
					fieldWithPath("username").type(STRING).description("This is username for login."),
					fieldWithPath("password").type(STRING).description("This is password for login.")
				),
				responseFields(
					fieldWithPath("status").type(STRING).description("Status value."),
					fieldWithPath("code").type(NUMBER).description("Status code."),
					fieldWithPath("data").type(STRING).description("This is login username.")
				)
			)
		);

		// when
		final Response response = given.when()
			.contentType(ContentType.JSON)
			.body(body.toString())
			.post(AUTHENTICATION);

		// then
		response.then()
			.statusCode(HttpStatus.OK.value())
			.header("Set-Cookie", containsString("JSESSIONID"));
	}

}
