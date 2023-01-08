package com.donetop.main.api.draft;

import com.donetop.enums.folder.FolderType;
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

public class DraftCreateTest extends IntegrationBase {

	@Autowired
	private DraftRepository draftRepository;

	@AfterAll
	void afterAll() throws IOException {
		draftRepository.deleteAll();
		FileSystemUtils.deleteRecursively(Path.of(applicationProperties.getStorage().getRoot()));
	}

	@Test
	void createOne_withoutParams_return400() {
		// given
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"draft_create/createOne_withoutParams_return400"
			)
		);

		// when
		final Response response = given.when()
			.post(SINGULAR);

		// then
		response.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("reason", hasSize(5));
	}

	@Test
	void createOne_withInvalidParams_return400() {
		// given
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"draft_create/createOne_withInvalidParams_return400"
			)
		);

		// when
		final Response response = given.when()
			.multiPart("customerName", "")
			.multiPart("price", 0)
			.multiPart("password", "")
			.post(SINGULAR);

		// then
		response.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("reason", hasSize(5));
	}

	@Test
	void createOne_withValidParamsAndWithoutFiles_return200() {
		// given
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"draft_create/createOne_withValidParamsAndWithoutFiles_return200"
			)
		);

		// when
		final Response response = given.when()
			.multiPart("customerName", "jin")
			.multiPart("address", "my address")
			.multiPart("price", 1000)
			.multiPart("memo", "simple test")
			.multiPart("password", "my password")
			.post(SINGULAR);

		// then
		response.then()
			.statusCode(HttpStatus.OK.value());
	}

	@Test
	void createOne_withValidParamsAndSizeExceedFiles_return400() {
		// given
		final Storage storage = applicationProperties.getStorage();
		final List<File> files = TestFileUtil.readFiles(Path.of(storage.getSrc()));
		final RequestSpecification given = RestAssured.given(this.spec);
		for (final File file : files) given.multiPart("files", file);
		given.filter(
			document(
				"draft_create/createOne_withValidParamsAndSizeExceedFiles_return400"
			)
		);

		// when
		final Response response = given.when()
			.multiPart("customerName", "jin")
			.multiPart("address", "my address")
			.multiPart("price", 1000)
			.multiPart("memo", "simple test")
			.multiPart("password", "my password")
			.post(SINGULAR);

		// then
		response.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("reason", containsString("SizeLimitExceededException"));
	}

	@Test
	void createOne_withValidParamsAndFiles_return200() throws Exception {
		// given
		final Storage storage = applicationProperties.getStorage();
		final List<File> files = TestFileUtil.readFiles(Path.of(storage.getSrc())).subList(0, 2);
		final RequestSpecification given = RestAssured.given(this.spec);
		for (final File file : files) given.multiPart("files", file);
		given.filter(
			document(
				"draft_create/createOne_withValidParamsAndFiles_return200",
				requestParts(
					partWithName("customerName").description("This parameter shouldn't be empty."),
					partWithName("price").description("This parameter should be greater or equal than 1000."),
					partWithName("address").description("This parameter shouldn't be null."),
					partWithName("memo").description("This parameter shouldn't be null."),
					partWithName("password").description("This parameter shouldn't be empty."),
					partWithName("files").description("This parameter can be empty. Each file's max size is 5MB.")
				),
				responseFields(
					fieldWithPath("status").type(STRING).description("Status value."),
					fieldWithPath("code").type(NUMBER).description("Status code."),
					fieldWithPath("data").type(NUMBER).description("This is auto generated draft id.")
				)
			)
		);

		// when
		final Response response = given.when()
			.multiPart("customerName", "jin")
			.multiPart("address", "my address")
			.multiPart("price", 1000)
			.multiPart("memo", "simple test")
			.multiPart("password", "my password")
			.post(SINGULAR);

		// then
		response.then().statusCode(HttpStatus.OK.value());
		final OK<String> ok = objectMapper.readValue(response.getBody().asString(), new TypeReference<>(){});
		final long draftId = Long.parseLong(ok.getData());
		final Path path = Path.of(FolderType.DRAFT.buildPathFrom(storage.getRoot(), draftId));
		assertThat(Objects.requireNonNull(path.toFile().listFiles()).length).isEqualTo(2);
	}

}
