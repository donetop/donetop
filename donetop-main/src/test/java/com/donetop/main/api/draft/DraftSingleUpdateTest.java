package com.donetop.main.api.draft;

import com.donetop.domain.entity.draft.Draft;
import com.donetop.enums.draft.DraftStatus;
import com.donetop.enums.folder.FolderType;
import com.donetop.enums.payment.PaymentMethod;
import com.donetop.main.api.common.IntegrationBase;
import com.donetop.main.api.common.Response.OK;
import com.donetop.main.common.TestFileUtil;
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

import static com.donetop.main.api.draft.DraftAPIController.Uri.SINGULAR;
import static com.donetop.main.properties.ApplicationProperties.Storage;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class DraftSingleUpdateTest extends IntegrationBase {

	@Autowired
	private DraftRepository draftRepository;

	@AfterAll
	void afterAll() throws IOException {
		draftRepository.deleteAll();
		FileSystemUtils.deleteRecursively(Path.of(applicationProperties.getStorage().getRoot()));
	}

	@Test
	void updateSingle_withInvalidParams_return400() {
		// given
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"draft_single_update/updateSingle_withInvalidParams_return400"
			)
		);

		// when
		final Response response = given.when()
			.multiPart("customerName", "")
			.multiPart("price", 100)
			.multiPart("password", "")
			.put(SINGULAR + "/{id}", 1);

		// then
		response.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("reason", hasSize(7));
	}

	@Test
	void updateSingle_withValidParamsButUnknownId_return400() {
	    // given
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"draft_single_update/updateSingle_withValidParamsButUnknownId_return400"
			)
		);

		// when
		final Response response = given.when()
			.multiPart("customerName", "mun")
			.multiPart("draftStatus", DraftStatus.COMPLETED.toString())
			.multiPart("address", "new address")
			.multiPart("price", 3000)
			.multiPart("paymentMethod", PaymentMethod.CHECK_CARD.toString())
			.multiPart("memo", "new memo")
			.multiPart("password", "new password")
			.put(SINGULAR + "/{id}", 100);

		// then
		response.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("reason", containsString("존재하지 않는 시안입니다."));
	}

	@Test
	void updateSingle_withValidParamsAndId_return200() {
		// given
		Draft draft = Draft.builder()
			.customerName("jin")
			.price(2000)
			.address("address")
			.memo("memo")
			.password("password").build();
		draftRepository.save(draft);
		assert draft.getId() != 0L;
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"draft_single_update/updateSingle_withValidParamsAndId_return200"
			)
		);

		// when
		final Response response = given.when()
			.multiPart("customerName", "mun")
			.multiPart("draftStatus", DraftStatus.COMPLETED.toString())
			.multiPart("address", "new address")
			.multiPart("price", 3000)
			.multiPart("paymentMethod", PaymentMethod.CHECK_CARD.toString())
			.multiPart("memo", "new memo")
			.multiPart("password", "new password")
			.put(SINGULAR + "/{id}", draft.getId());

		// then
		response.then()
			.statusCode(HttpStatus.OK.value())
			.body("data", is(Integer.valueOf(String.valueOf(draft.getId()))));
	}

	@Test
	void updateSingle_withValidParamsAndFilesAndId_return200() throws Exception {
		// given
		Draft draft = Draft.builder()
			.customerName("jin")
			.price(2000)
			.address("address")
			.memo("memo")
			.password("password").build();
		draftRepository.save(draft);
		assert draft.getId() != 0L;
		final Storage storage = applicationProperties.getStorage();
		final List<java.io.File> files = TestFileUtil.readFiles(Path.of(storage.getSrc())).subList(0, 2);
		final RequestSpecification given = RestAssured.given(this.spec);
		for (final File file : files) given.multiPart("files", file);
		given.filter(
			document(
				"draft_single_update/updateSingle_withValidParamsAndFilesAndId_return200",
				requestParts(
					partWithName("customerName").description("This field shouldn't be empty."),
					partWithName("price").description("This field should be greater or equal than 1000."),
					partWithName("address").description("This field shouldn't be null."),
					partWithName("memo").description("This field shouldn't be null."),
					partWithName("password").description("This field shouldn't be empty."),
					partWithName("draftStatus").description("This field shouldn't be null."),
					partWithName("paymentMethod").description("This field shouldn't be null."),
					partWithName("files").optional().description("This parameter can be empty. Each file's max size is 5MB.")
				),
				responseFields(
					fieldWithPath("status").type(STRING).description("Status value."),
					fieldWithPath("code").type(NUMBER).description("Status code."),
					fieldWithPath("data").type(NUMBER).description("This is the original draft id.")
				)
			)
		);

		// when
		final Response response = given.when()
			.multiPart("customerName", "mun")
			.multiPart("draftStatus", DraftStatus.COMPLETED.toString())
			.multiPart("address", "new address")
			.multiPart("price", 3000)
			.multiPart("paymentMethod", PaymentMethod.CHECK_CARD.toString())
			.multiPart("memo", "new memo")
			.multiPart("password", "new password")
			.put(SINGULAR + "/{id}", draft.getId());

		// then
		response.then()
			.statusCode(HttpStatus.OK.value())
			.body("data", is(Integer.valueOf(String.valueOf(draft.getId()))));
		final OK<String> ok = objectMapper.readValue(response.getBody().asString(), new TypeReference<>(){});
		final long draftId = Long.parseLong(ok.getData());
		final Path path = Path.of(FolderType.DRAFT.buildPathFrom(storage.getRoot(), draftId));
		assertThat(Objects.requireNonNull(path.toFile().listFiles()).length).isEqualTo(2);
	}

}
