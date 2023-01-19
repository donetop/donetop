package com.donetop.main.api.form;

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

import static com.donetop.main.api.form.FormAPIController.Uri.*;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.*;

public class FormLoginTest extends IntegrationBase {

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
	void login_withEmptyBody_return400() {
		// given
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"form_login/login_withEmptyBody_return400"
			)
		);

		// when
		final Response response = given.when()
			.contentType(ContentType.JSON)
			.post(LOGIN);

		// then
		response.then()
			.statusCode(HttpStatus.BAD_REQUEST.value());
	}

	@Test
	void login_withIncorrectFields_return400() throws Exception {
		// given
		final JSONObject body = new JSONObject();
		body.put("usernamee", "jin");
		body.put("passworddd", "password");
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"form_login/login_withIncorrectFields_return400"
			)
		);

		// when
		final Response response = given.when()
			.contentType(ContentType.JSON)
			.body(body.toString())
			.post(LOGIN);

		// then
		response.then()
			.statusCode(HttpStatus.BAD_REQUEST.value());
	}

	@Test
	void login_withNullValues_return400() throws Exception {
		// given
		final JSONObject body = new JSONObject();
		body.put("username", JSONObject.NULL);
		body.put("password", JSONObject.NULL);
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"form_login/login_withNullValues_return400"
			)
		);

		// when
		final Response response = given.when()
			.contentType(ContentType.JSON)
			.body(body.toString())
			.post(LOGIN);

		// then
		response.then()
			.statusCode(HttpStatus.BAD_REQUEST.value());
	}

	@Test
	void login_withEmptyValues_return400() throws Exception {
		// given
		final JSONObject body = new JSONObject();
		body.put("username", "");
		body.put("password", "");
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"form_login/login_withEmptyValues_return400"
			)
		);

		// when
		final Response response = given.when()
			.contentType(ContentType.JSON)
			.body(body.toString())
			.post(LOGIN);

		// then
		response.then()
			.statusCode(HttpStatus.BAD_REQUEST.value());
	}

	@Test
	void login_withWrongUsername_return400() throws Exception {
		// given
		final JSONObject body = new JSONObject();
		body.put("username", "unknown");
		body.put("password", "unknown");
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"form_login/login_withWrongUsername_return400"
			)
		);

		// when
		final Response response = given.when()
			.contentType(ContentType.JSON)
			.body(body.toString())
			.post(LOGIN);

		// then
		response.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("reason", containsString("유저이름이 유효하지 않습니다."));
	}

	@Test
	void login_withWrongPassword_return400() throws Exception {
		// given
		final JSONObject body = new JSONObject();
		body.put("username", "jin");
		body.put("password", "unknown");
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"form_login/login_withWrongPassword_return400"
			)
		);

		// when
		final Response response = given.when()
			.contentType(ContentType.JSON)
			.body(body.toString())
			.post(LOGIN);

		// then
		response.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("reason", containsString("비밀번호가 유효하지 않습니다."));
	}

	@Test
	void login_withValidInfo_return200() throws Exception {
		// given
		final JSONObject body = new JSONObject();
		body.put("username", "jin");
		body.put("password", "my password");
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"form_login/login_withValidInfo_return200",
				requestFields(
					fieldWithPath("username").type(STRING).description("This is username for login."),
					fieldWithPath("password").type(STRING).description("This is password for login."),
					fieldWithPath("autoLogin").type(BOOLEAN).optional().description("This indicates autoLogin. The default value is false.")
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
			.post(LOGIN);

		// then
		response.then()
			.statusCode(HttpStatus.OK.value())
			.header("Set-Cookie", containsString("JSESSIONID"));
	}

}
