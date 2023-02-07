package com.donetop.main.api.draft;

import com.donetop.domain.entity.draft.Draft;
import com.donetop.domain.entity.folder.Folder;
import com.donetop.main.api.common.IntegrationBase;
import com.donetop.main.common.TestFileUtil;
import com.donetop.main.service.storage.Resource;
import com.donetop.main.service.storage.StorageService;
import com.donetop.repository.draft.DraftRepository;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.FileSystemUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import static com.donetop.main.api.draft.DraftAPIController.Uri.*;
import static com.donetop.main.properties.ApplicationProperties.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class DraftSingleGetTest extends IntegrationBase {

	@Autowired
	private DraftRepository draftRepository;

	@Autowired
	private StorageService storageService;

	@AfterAll
	void afterAll() throws IOException {
		draftRepository.deleteAll();
		FileSystemUtils.deleteRecursively(Path.of(applicationProperties.getStorage().getRoot()));
	}

	@Test
	void getSingle_withInvalidId_return400() {
	    // given
		final long id = 10000;
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"draft_single_get/getSingle_withInvalidId_return400"
			)
		);

	    // when
		final Response response = given.when()
			.get(SINGULAR + "/{id}", id);

		// then
		response.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("reason", containsString("존재하지 않는 시안입니다."));
	}

	@Test
	void getSingle_withValidIdButWrongPassword_return400() {
		// given
		final LocalDateTime now = LocalDateTime.now();
		final Draft draft = new Draft().toBuilder()
			.customerName("jin")
			.companyName("jin's company")
			.email("jin@test.com")
			.category("category")
			.phoneNumber("010-0000-0000")
			.address("my address")
			.price(10000L)
			.memo("get test")
			.password("my password")
			.createTime(now)
			.updateTime(now).build();
		draftRepository.save(draft);
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"draft_single_get/getSingle_withValidIdButWrongPassword_return400"
			)
		);

		// when
		final Response response = given.when()
			.param("password", "wrong password")
			.get(SINGULAR + "/{id}", draft.getId());

		// then
		response.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("reason", containsString("비밀번호가 일치하지 않습니다."));
	}

	@Test
	void getSingle_withValidIdAndRightPassword_return200() {
		// given
		final LocalDateTime now = LocalDateTime.now();
		final Draft draft = new Draft().toBuilder()
			.customerName("jin")
			.companyName("jin's company")
			.email("jin@test.com")
			.category("category")
			.phoneNumber("010-0000-0000")
			.address("my address")
			.price(10000L)
			.memo("get test")
			.password("my password")
			.createTime(now)
			.updateTime(now).build();
		draftRepository.save(draft);
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"draft_single_get/getSingle_withValidIdAndRightPassword_return200"
			)
		);

		// when
		final Response response = given.when()
			.param("password", draft.getPassword())
			.get(SINGULAR + "/{id}", draft.getId());

		// then
		response.then()
			.statusCode(HttpStatus.OK.value())
			.body("data.customerName", is(draft.getCustomerName()))
			.body("data.draftStatus", is(draft.getDraftStatus().toString()))
			.body("data.address", is(draft.getAddress()))
			.body("data.price", is(Integer.valueOf(String.valueOf(draft.getPrice()))))
			.body("data.paymentMethod", is(draft.getPaymentMethod().toString()))
			.body("data.memo", is(draft.getMemo()));
	}

	@Test
	void getSingleThatHasFolder_withValidIdAndRightPassword_return200() {
		// given
		final Storage storage = applicationProperties.getStorage();
		final List<Resource> resources = TestFileUtil.readResources(Path.of(storage.getSrc()));
		final LocalDateTime now = LocalDateTime.now();
		final Draft draft = new Draft().toBuilder()
			.customerName("jin")
			.companyName("jin's company")
			.email("jin@test.com")
			.category("category")
			.phoneNumber("010-0000-0000")
			.address("my address")
			.price(10000L)
			.memo("get test")
			.password("my password")
			.createTime(now)
			.updateTime(now).build();
		draftRepository.save(draft);
		Folder folder = draft.getOrNewFolder(storage.getRoot());
		storageService.save(resources, folder);
		draft.addFolder(folder);
		draftRepository.save(draft);
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"draft_single_get/getSingleThatHasFolder_withValidIdAndRightPassword_return200",
				requestParameters(
					parameterWithName("password").description("Draft password.")
				),
				responseFields(
					fieldWithPath("status").type(STRING).description("Status value."),
					fieldWithPath("code").type(NUMBER).description("Status code."),
					fieldWithPath("data").type(OBJECT).description("Response data."),
					fieldWithPath("data.id").type(NUMBER).description("Draft id."),
					fieldWithPath("data.customerName").description("Draft customerName."),
					fieldWithPath("data.companyName").description("Draft companyName."),
					fieldWithPath("data.email").description("Draft email."),
					fieldWithPath("data.phoneNumber").description("Draft phoneNumber."),
					fieldWithPath("data.category").description("Draft category."),
					fieldWithPath("data.draftStatus").type(STRING).description("Draft status."),
					fieldWithPath("data.address").type(STRING).description("Draft address."),
					fieldWithPath("data.price").description("Draft price."),
					fieldWithPath("data.paymentMethod").type(STRING).description("Draft paymentMethod."),
					fieldWithPath("data.memo").type(STRING).description("Draft memo."),
					fieldWithPath("data.createTime").type(STRING).description("Draft create time."),
					fieldWithPath("data.updateTime").type(STRING).description("Draft update time."),
					subsectionWithPath("data.folder").type(OBJECT).description("Draft's folder."),
					subsectionWithPath("data.folder.files").type(ARRAY).description("The files that are included in a folder.")
				)
			)
		);

		// when
		final Response response = given.when()
			.param("password", draft.getPassword())
			.get(SINGULAR + "/{id}", draft.getId());

		// then
		response.then()
			.statusCode(HttpStatus.OK.value())
			.body("data.customerName", is(draft.getCustomerName()))
			.body("data.draftStatus", is(draft.getDraftStatus().toString()))
			.body("data.address", is(draft.getAddress()))
			.body("data.price", is(Integer.valueOf(String.valueOf(draft.getPrice()))))
			.body("data.paymentMethod", is(draft.getPaymentMethod().toString()))
			.body("data.memo", is(draft.getMemo()))
			.body("data.folder.files", hasSize(4));
	}
}
