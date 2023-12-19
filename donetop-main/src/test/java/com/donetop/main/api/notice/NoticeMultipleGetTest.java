package com.donetop.main.api.notice;

import com.donetop.domain.entity.notice.Notice;
import com.donetop.main.api.common.NoticeBase;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static com.donetop.main.api.notice.NoticeAPIController.URI.PLURAL;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class NoticeMultipleGetTest extends NoticeBase {

	List<Notice> notices;

	@BeforeAll
	void beforeAll() {
		notices = saveMultipleNoticesWithFiles();
	}

	@Test
	void getMultiple_notice_return200() {
		// given
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"notice_multiple_get/getMultiple_notice_return200",
				responseFields(
					fieldWithPath("status").type(STRING).description("Status value."),
					fieldWithPath("code").type(NUMBER).description("Status code."),
					subsectionWithPath("data").type(ARRAY).description("Searched notices.")
				)
			)
		);

		// when
		final Response response = given.when().get(PLURAL);

		// then
		response.then()
			.statusCode(HttpStatus.OK.value())
			.body("data", hasSize(notices.size()));
	}

}
