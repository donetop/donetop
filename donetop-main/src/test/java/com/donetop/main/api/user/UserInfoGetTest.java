package com.donetop.main.api.user;

import com.donetop.domain.entity.user.User;
import com.donetop.enums.user.RoleType;
import com.donetop.main.api.common.UserBase;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.format.DateTimeFormatter;

import static com.donetop.common.api.Message.NO_SESSION;
import static com.donetop.main.api.user.UserAPIController.URI.SINGULAR;
import static org.hamcrest.Matchers.is;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class UserInfoGetTest extends UserBase {

	@Test
	void getUserInfo_withoutSession_return400() {
		// given
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"user_info_get/getUserInfo_withoutSession_return400"
			)
		);

		// when
		final Response response = given.when()
			.get(SINGULAR);

		// then
		response.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("reason", is(NO_SESSION))
		;
	}

	@Test
	void getUserInfo_withSession_return200() throws Exception {
		// given
		final User jin = saveUser("jin", RoleType.NORMAL);
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"user_info_get/getUserInfo_withSession_return200",
				responseFields(
					fieldWithPath("status").type(STRING).description("Status value."),
					fieldWithPath("code").type(NUMBER).description("Status code."),
					fieldWithPath("data").type(OBJECT).description("Response data."),
					fieldWithPath("data.id").type(NUMBER).description("User id."),
					fieldWithPath("data.email").type(STRING).description("User email."),
					fieldWithPath("data.name").type(STRING).description("User name."),
					fieldWithPath("data.roleType").type(STRING).description("User roleType."),
					fieldWithPath("data.createTime").type(STRING).description("User createTime."),
					fieldWithPath("data.admin").type(BOOLEAN).description("Admin status.")
				)
			)
		);

		// when
		final Response response = given.when()
			.cookies(doLoginWith(jin).cookies())
			.get(SINGULAR);

		// then
		response.then()
			.statusCode(HttpStatus.OK.value())
			.body("data.id", is((int) jin.getId()))
			.body("data.email", is(jin.getEmail()))
			.body("data.name", is(jin.getName()))
			.body("data.roleType", is(jin.getRoleType().name()))
			.body("data.createTime", is(jin.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))))
			.body("data.admin", is(false))
		;
	}

}
