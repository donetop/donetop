package com.donetop.main.api.sms;

import com.donetop.main.api.common.PhoneVerificationBase;
import com.donetop.main.api.sms.request.VerificationCodeSendRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static com.donetop.common.api.Message.*;
import static com.donetop.main.api.sms.PhoneVerificationController.URI.ENDPOINT;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class PhoneVerificationSendTest extends PhoneVerificationBase {

	@Test
	void send_withInvalidPhoneNumberFormat_return400() {
		// given
		final String invalidPhoneNumber = "01012345678";
		final VerificationCodeSendRequest request = new VerificationCodeSendRequest();
		request.setPhoneNumber(invalidPhoneNumber);

		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"phone_verification_send/send_withInvalidPhoneNumberFormat_return400"
			)
		);

		// when
		final Response response = given
			.contentType(ContentType.JSON)
			.body(request)
			.when()
			.post(ENDPOINT + "/send");

		// then
		response.then()
			.statusCode(HttpStatus.BAD_REQUEST.value());
	}

	@Test
	void send_exceedDailyLimit_return400() {
		// given
		final String phoneNumber = "010-9999-8888";
		for (int i = 0; i < 5; i++) {
			verificationCodeRepository.incrementDailyCount(phoneNumber);
		}

		final VerificationCodeSendRequest request = new VerificationCodeSendRequest();
		request.setPhoneNumber(phoneNumber);

		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"phone_verification_send/send_exceedDailyLimit_return400"
			)
		);

		// when
		final Response response = given
			.contentType(ContentType.JSON)
			.body(request)
			.when()
			.post(ENDPOINT + "/send");

		// then
		response.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("reason", containsString(SMS_SEND_LIMIT_EXCEEDED));
	}

	@Test
	void send_withValidPhoneNumber_return200() {
		// given
		final String phoneNumber = "010-1234-5678";
		final VerificationCodeSendRequest request = new VerificationCodeSendRequest();
		request.setPhoneNumber(phoneNumber);

		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"phone_verification_send/send_withValidPhoneNumber_return200",
				requestFields(
					fieldWithPath("phoneNumber").type(STRING).description("인증번호를 수신할 휴대폰 번호 (하이픈 포함)")
				),
				responseFields(
					fieldWithPath("status").type(STRING).description("응답 상태 문자열"),
					fieldWithPath("code").type(NUMBER).description("응답 코드"),
					fieldWithPath("data").type(STRING).description("응답 메시지")
				)
			)
		);

		// when
		final Response response = given
			.contentType(ContentType.JSON)
			.body(request)
			.when()
			.post(ENDPOINT + "/send");

		// then
		response.then()
			.statusCode(HttpStatus.OK.value())
			.body("status", is("OK"))
			.body("data", is(SMS_SEND_SUCCESS));
	}
}
