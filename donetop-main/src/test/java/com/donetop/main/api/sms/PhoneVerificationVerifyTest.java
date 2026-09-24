package com.donetop.main.api.sms;

import com.donetop.common.api.Message;
import com.donetop.main.api.common.PhoneVerificationBase;
import com.donetop.main.api.sms.request.VerificationCodeVerifyRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static com.donetop.main.api.sms.PhoneVerificationController.URI.ENDPOINT;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class PhoneVerificationVerifyTest extends PhoneVerificationBase {

	@Test
	void verify_withInvalidCode_return400() {
		// given
		final String phoneNumber = "010-1234-5678";
		verificationCodeRepository.save(phoneNumber, "123456", 180L);

		final VerificationCodeVerifyRequest request = new VerificationCodeVerifyRequest();
		request.setPhoneNumber(phoneNumber);
		request.setCode("000000");

		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"phone_verification_verify/verify_withInvalidCode_return400"
			)
		);

		// when
		final Response response = given
			.contentType(ContentType.JSON)
			.body(request)
			.when()
			.post(ENDPOINT + "/verify");

		// then
		response.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("reason", containsString(Message.SMS_VERIFY_FAIL));
	}

	@Test
	void verify_withNonExistentOrExpiredCode_return400() {
		// given
		final VerificationCodeVerifyRequest request = new VerificationCodeVerifyRequest();
		request.setPhoneNumber("010-0000-0000");
		request.setCode("123456");

		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"phone_verification_verify/verify_withNonExistentOrExpiredCode_return400"
			)
		);

		// when
		final Response response = given
			.contentType(ContentType.JSON)
			.body(request)
			.when()
			.post(ENDPOINT + "/verify");

		// then
		response.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("reason", containsString(Message.SMS_VERIFY_FAIL));
	}

	@Test
	void verify_withValidCode_return200() {
		// given
		final String phoneNumber = "010-1234-5678";
		final String code = "123456";
		verificationCodeRepository.save(phoneNumber, code, 180L);

		final VerificationCodeVerifyRequest request = new VerificationCodeVerifyRequest();
		request.setPhoneNumber(phoneNumber);
		request.setCode(code);

		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"phone_verification_verify/verify_withValidCode_return200",
				requestFields(
					fieldWithPath("phoneNumber").type(STRING).description("인증을 진행할 휴대폰 번호"),
					fieldWithPath("code").type(STRING).description("수신한 6자리 인증번호")
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
			.post(ENDPOINT + "/verify");

		// then
		response.then()
			.statusCode(HttpStatus.OK.value())
			.body("status", is("OK"))
			.body("data", is(Message.SMS_VERIFY_SUCCESS));
	}
}
