package com.donetop.main.api.enums;

import com.donetop.main.api.common.IntegrationBase;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static com.donetop.main.api.enums.EnumAPIController.URI.DRAFT_STATUS;
import static com.donetop.main.api.enums.EnumAPIController.URI.PAYMENT_METHOD;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class EnumListGetTest extends IntegrationBase {

	@Test
	void getList_draftStatus_return200() {
		// given
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"enum_list_get/getList_draftStatus_return200",
				responseFields(
					fieldWithPath("status").type(STRING).description("Status value."),
					fieldWithPath("code").type(NUMBER).description("Status code."),
					fieldWithPath("data").type(ARRAY).description("Response data."),
					fieldWithPath("data[].name").type(STRING).description("Enum name."),
					fieldWithPath("data[].value").type(STRING).description("Enum value.")
				)
			)
		);

		// when
		final Response response = given.when().get(DRAFT_STATUS);

		// then
		response.then()
			.statusCode(HttpStatus.OK.value())
			.body("data", hasSize(5));
	}

	@Test
	void getList_paymentMethod_return200() {
		// given
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"enum_list_get/getList_paymentMethod_return200",
				responseFields(
					fieldWithPath("status").type(STRING).description("Status value."),
					fieldWithPath("code").type(NUMBER).description("Status code."),
					fieldWithPath("data").type(ARRAY).description("Response data."),
					fieldWithPath("data[].name").type(STRING).description("Enum name."),
					fieldWithPath("data[].value").type(STRING).description("Enum value.")
				)
			)
		);

		// when
		final Response response = given.when().get(PAYMENT_METHOD);

		// then
		response.then()
			.statusCode(HttpStatus.OK.value())
			.body("data", hasSize(2));
	}

}
