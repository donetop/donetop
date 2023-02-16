package com.donetop.main.api.draft;

import com.donetop.enums.draft.Category;
import com.donetop.enums.folder.FolderType;
import com.donetop.enums.draft.PaymentMethod;
import com.donetop.main.api.common.IntegrationBase;
import com.donetop.main.common.TestFileUtil;
import com.donetop.main.properties.ApplicationProperties.Storage;
import com.donetop.repository.draft.DraftRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import static com.donetop.main.api.common.Response.OK;
import static com.donetop.main.api.draft.DraftAPIController.Uri.SINGULAR;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class DraftSingleCreateTest extends IntegrationBase {

	@Autowired
	private DraftRepository draftRepository;

	@AfterAll
	void afterAll() throws IOException {
		draftRepository.deleteAll();
		FileSystemUtils.deleteRecursively(Path.of(applicationProperties.getStorage().getRoot()));
	}

	@Test
	void createSingle_withoutParts_return400() {
		// given
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"draft_single_create/createSingle_withoutParts_return400"
			)
		);

		// when
		final Response response = given.when()
			.post(SINGULAR);

		// then
		response.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("reason", hasSize(9));
	}

	@Test
	void createSingle_withInvalidPartValues_return400() {
		// given
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"draft_single_create/createSingle_withInvalidPartValues_return400"
			)
		);

		// when
		final Response response = given.when()
			.multiPart("customerName", "")
			.multiPart("companyName", "")
			.multiPart("email", "")
			.multiPart("category", "")
			.multiPart("phoneNumber", "")
			.multiPart("address", "")
			.multiPart("memo", "")
			.multiPart("password", "")
			.multiPart("paymentMethod", "asdsdsadsa")
			.post(SINGULAR);

		// then
		response.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("reason", hasSize(7));
	}

	@Test
	void createSingle_withValidPartValues_return200() {
		// given
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"draft_single_create/createSingle_withValidPartValues_return200"
			)
		);

		// when
		final Response response = given.when()
			.multiPart("customerName", "jin")
			.multiPart("companyName", "my company")
			.multiPart("email", "jin@test.com")
			.multiPart("category", Category.BAENEO.toString())
			.multiPart("phoneNumber", "010-0000-0000")
			.multiPart("address", "my address")
			.multiPart("memo", "my memo")
			.multiPart("password", "my password")
			.multiPart("paymentMethod", PaymentMethod.CASH.toString())
			.post(SINGULAR);

		// then
		response.then()
			.statusCode(HttpStatus.OK.value());
	}

	@Test
	void createSingle_withValidPartValuesAndSizeExceedFiles_return400() {
		// given
		final Storage storage = applicationProperties.getStorage();
		final List<File> files = TestFileUtil.readFiles(Path.of(storage.getSrc()));
		final RequestSpecification given = RestAssured.given(this.spec);
		for (final File file : files) given.multiPart("files", file);
		given.filter(
			document(
				"draft_single_create/createSingle_withValidPartValuesAndSizeExceedFiles_return400"
			)
		);

		// when
		final Response response = given.when()
			.multiPart("customerName", "jin")
			.multiPart("companyName", "my company")
			.multiPart("email", "jin@test.com")
			.multiPart("category", "my category")
			.multiPart("phoneNumber", "010-0000-0000")
			.multiPart("address", "my address")
			.multiPart("memo", "my memo")
			.multiPart("password", "my password")
			.multiPart("paymentMethod", PaymentMethod.CASH.toString())
			.post(SINGULAR);

		// then
		response.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("reason", containsString("5MB"));
	}

	@Test
	void createSingle_withValidPartValuesAndFiles_return200() throws Exception {
		// given
		final Storage storage = applicationProperties.getStorage();
		final List<File> files = TestFileUtil.readFiles(Path.of(storage.getSrc())).subList(0, 2);
		final RequestSpecification given = RestAssured.given(this.spec);
		for (final File file : files) given.multiPart("files", file);
		given.filter(
			document(
				"draft_single_create/createSingle_withValidPartValuesAndFiles_return200",
				requestParts(
					partWithName("customerName").description("The value shouldn't be empty."),
					partWithName("companyName").description("The value can be empty."),
					partWithName("email").description("The value shouldn't be empty."),
					partWithName("category").description("The value shouldn't be empty."),
					partWithName("phoneNumber").description("The value shouldn't be empty."),
					partWithName("address").description("The value shouldn't be empty."),
					partWithName("memo").description("The value can be empty."),
					partWithName("password").description("The value shouldn't be empty."),
					partWithName("paymentMethod").description("The value should be one of [CASH, CREDIT_CARD]."),
					partWithName("files").description("The value can be empty. Each file's max size is 5MB.")
				),
				responseFields(
					fieldWithPath("status").type(STRING).description("Status value."),
					fieldWithPath("code").type(NUMBER).description("Status code."),
					fieldWithPath("data").type(NUMBER).description("This is the auto generated draft id.")
				)
			)
		);

		// when
		final Response response = given.when()
			.multiPart("customerName", "jin")
			.multiPart("companyName", "my company")
			.multiPart("email", "jin@test.com")
			.multiPart("category", Category.BAENEO.toString())
			.multiPart("phoneNumber", "010-0000-0000")
			.multiPart("address", "my address")
			.multiPart("memo", "my memo")
			.multiPart("password", "my password")
			.multiPart("paymentMethod", PaymentMethod.CASH.toString())
			.post(SINGULAR);

		// then
		response.then().statusCode(HttpStatus.OK.value());
		final OK<String> ok = objectMapper.readValue(response.getBody().asString(), new TypeReference<>(){});
		final long draftId = Long.parseLong(ok.getData());
		final Path path = Path.of(FolderType.DRAFT.buildPathFrom(storage.getRoot(), draftId));
		assertThat(Objects.requireNonNull(path.toFile().listFiles()).length).isEqualTo(2);
	}

}
