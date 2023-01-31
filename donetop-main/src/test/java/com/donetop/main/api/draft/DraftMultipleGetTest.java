package com.donetop.main.api.draft;

import com.donetop.domain.entity.draft.Draft;
import com.donetop.main.api.common.IntegrationBase;
import com.donetop.repository.draft.DraftRepository;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.donetop.main.api.draft.DraftAPIController.Uri.PLURAL;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class DraftMultipleGetTest extends IntegrationBase {

	@Autowired
	private DraftRepository draftRepository;

	private final List<Draft> drafts = new ArrayList<>();

	@BeforeAll
	void beforeAll() {
		LocalDateTime now = LocalDateTime.now();
		for (int i = 0; i < 100; i++) {
			Draft draft = new Draft().toBuilder()
				.customerName("jin" + i)
				.companyName("jin's company")
				.email("jin@test.com")
				.category("category")
				.phoneNumber("010-0000-0000")
				.price(1000 + i)
				.address("address" + i)
				.memo("memo" + i)
				.password("password" + i)
				.createTime(now)
				.updateTime(now).build();
			drafts.add(draft);
			now = now.plusDays(1L);
		}
		draftRepository.saveAll(drafts);
	}

	@AfterAll
	void afterAll() {
		draftRepository.deleteAll();
	}

	@Test
	void getMultiple_withInvalidPageValue_return400() {
		// given
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"draft_multiple_get/getMultiple_withInvalidPageValue_return400"
			)
		);

		// when & then
		given
			.when()
				.param("page", -1)
				.get(PLURAL)
			.then()
				.statusCode(HttpStatus.BAD_REQUEST.value())
				.body("reason", is("Page index must not be less than zero"));
	}

	@Test
	void getMultiple_withInvalidSizeValue_return400() {
		// given
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"draft_multiple_get/getMultiple_withInvalidSizeValue_return400"
			)
		);

		// when & then
		given
			.when()
				.param("size", -1)
				.get(PLURAL)
			.then()
				.statusCode(HttpStatus.BAD_REQUEST.value())
				.body("reason", is("Page size must not be less than one"));
	}

	@Test
	void getMultiple_withInvalidDirectionValue_return400() {
		// given
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"draft_multiple_get/getMultiple_withInvalidDirectionValue_return400"
			)
		);

		// when & then
		given
			.when()
				.param("direction", "adesc")
				.get(PLURAL)
			.then()
				.statusCode(HttpStatus.BAD_REQUEST.value())
				.body("reason", is("Invalid value 'adesc' for orders given; Has to be either 'desc' or 'asc' (case insensitive)"));
	}

	@Test
	void getMultiple_withInvalidPropertyValue_return400() {
		// given
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"draft_multiple_get/getMultiple_withInvalidPropertyValue_return400"
			)
		);

		// when & then
		given
			.when()
				.param("property", "iddd")
				.get(PLURAL)
			.then()
				.statusCode(HttpStatus.BAD_REQUEST.value())
				.body("reason", is("No property 'iddd' found for type 'Draft' Did you mean ''id''"));
	}

	@Test
	void getMultiple_withValidParams_return200() {
		// given
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"draft_multiple_get/getMultiple_withValidParams_return200",
				requestParameters(
					parameterWithName("page").description("The page to retrieve. Default is 0."),
					parameterWithName("size").description("The size to retrieve. Default is 20."),
					parameterWithName("property").description("The property for sorting. Default is \"createTime\"."),
					parameterWithName("direction").description("The direction of property. Default is \"desc\".")
				),
				responseFields(
					fieldWithPath("status").type(STRING).description("Status value."),
					fieldWithPath("code").type(NUMBER).description("Status code."),
					fieldWithPath("data").type(OBJECT).description("Response data."),
					subsectionWithPath("data.content").type(ARRAY).description("Searched Drafts. See the Get One Draft response body field description for details."),
					subsectionWithPath("data.pageable").type(OBJECT).description("Pageable information."),
					fieldWithPath("data.last").type(BOOLEAN).description("Last information."),
					fieldWithPath("data.totalElements").type(NUMBER).description("TotalElements information."),
					fieldWithPath("data.totalPages").type(NUMBER).description("TotalPages information."),
					fieldWithPath("data.size").type(NUMBER).description("Size information."),
					fieldWithPath("data.number").type(NUMBER).description("Number information."),
					subsectionWithPath("data.sort").type(OBJECT).description("Sort information."),
					fieldWithPath("data.first").type(BOOLEAN).description("First information."),
					fieldWithPath("data.numberOfElements").type(NUMBER).description("NumberOfElements information."),
					fieldWithPath("data.empty").type(BOOLEAN).description("Empty information.")
				)
			)
		);

		// when
		final Response response = given.when()
			.param("page", 1)
			.param("size", 5)
			.param("direction", "desc")
			.param("property", "price")
			.get(PLURAL);

		// then
		response.then()
			.statusCode(HttpStatus.OK.value())
			.body("data.content", hasSize(5))
			.body("data.first", is(false))
			.body("data.totalPages", is(20))
			.body("data.totalElements", is(100));
	}

	@Test
	void getMultiple_withoutParams_return200() {
		// given
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"draft_multiple_get/getMultiple_withoutParams_return200"
			)
		);

		// when & then
		given
			.when()
				.get(PLURAL)
			.then()
				.statusCode(HttpStatus.OK.value())
				.body("data.content", hasSize(20))
				.body("data.first", is(true))
				.body("data.totalPages", is(5))
				.body("data.totalElements", is(100));
	}
}
