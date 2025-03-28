package com.donetop.main.api.draft;

import com.donetop.domain.entity.draft.Draft;
import com.donetop.enums.draft.DraftStatus;
import com.donetop.enums.draft.PaymentMethod;
import com.donetop.main.api.common.DraftBase;
import com.donetop.common.api.Response.OK;
import com.donetop.common.service.storage.LocalFileUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import static com.donetop.common.api.Message.UNKNOWN_DRAFT;
import static com.donetop.enums.folder.DomainType.DRAFT;
import static com.donetop.enums.folder.FolderType.DRAFT_WORK;
import static com.donetop.main.api.draft.DraftAPIController.URI.SINGULAR;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class DraftSingleUpdateTest extends DraftBase {

	@Test
	void updateSingle_withInvalidPartValues_return400() {
		// given
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"draft_single_update/updateSingle_withInvalidPartValues_return400"
			)
		);

		// when
		final Response response = given.when()
			.multiPart("customerName", "")
			.multiPart("companyName", "")
			.multiPart("inChargeName", "")
			.multiPart("email", "")
			.multiPart("categoryName", "")
			.multiPart("phoneNumber", "")
			.multiPart("address", "")
			.multiPart("detailAddress", "")
			.multiPart("memo", "")
			.multiPart("estimateContent", "")
			.multiPart("password", "")
			.multiPart("paymentMethod", "asdsdsadsa")
			.multiPart("price", -1)
			.multiPart("draftStatus", "fdfdfe")
			.put(SINGULAR + "/{id}", 1);

		// then
		response.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("reason", hasSize(8));
	}

	@Test
	void updateSingle_withValidPartValuesButUnknownId_return400() {
	    // given
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"draft_single_update/updateSingle_withValidPartValuesButUnknownId_return400"
			)
		);

		// when
		final Response response = given.when()
			.multiPart("customerName", "jin")
			.multiPart("companyName", "my company")
			.multiPart("inChargeName", "hak")
			.multiPart("email", "jin@test.com")
			.multiPart("categoryName", "봉투")
			.multiPart("phoneNumber", "010-0000-0000")
			.multiPart("address", "my address")
			.multiPart("detailAddress", "my detail address")
			.multiPart("memo", "my memo")
			.multiPart("estimateContent", "my estimate content")
			.multiPart("password", "my password")
			.multiPart("paymentMethod", PaymentMethod.CREDIT_CARD.toString())
			.multiPart("price", 3000)
			.multiPart("draftStatus", DraftStatus.COMPLETED.toString())
			.put(SINGULAR + "/{id}", 100);

		// then
		response.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("reason", containsString(UNKNOWN_DRAFT));
	}

	@Test
	void updateSingle_withValidPartValuesAndId_return200() {
		// given
		final Draft draft = saveSingleDraftWithoutFiles();
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"draft_single_update/updateSingle_withValidPartValuesAndId_return200"
			)
		);

		// when
		final Response response = given.when()
			.multiPart("customerName", "jin")
			.multiPart("companyName", "my company")
			.multiPart("inChargeName", "hak")
			.multiPart("email", "jin@test.com")
			.multiPart("categoryName", "봉투")
			.multiPart("phoneNumber", "010-0000-0000")
			.multiPart("address", "my address")
			.multiPart("detailAddress", "my detail address")
			.multiPart("memo", "my memo")
			.multiPart("estimateContent", "my estimate content")
			.multiPart("password", "my password")
			.multiPart("paymentMethod", PaymentMethod.CREDIT_CARD.toString())
			.multiPart("price", 3000)
			.multiPart("draftStatus", DraftStatus.COMPLETED.toString())
			.put(SINGULAR + "/{id}", draft.getId());

		// then
		response.then()
			.statusCode(HttpStatus.OK.value())
			.body("data", is(Integer.valueOf(String.valueOf(draft.getId()))));
	}

	@Test
	void updateSingle_withValidPartValuesAndFilesAndId_return200() throws Exception {
		// given
		final Draft draft = saveSingleDraftWithoutFiles();
		final List<java.io.File> files = LocalFileUtil.readFiles(Path.of(testStorage.getSrc())).subList(0, 1);
		final RequestSpecification given = RestAssured.given(this.spec);
		for (final File file : files) given.multiPart("files", file);
		given.filter(
			document(
				"draft_single_update/updateSingle_withValidPartValuesAndFilesAndId_return200",
				requestParts(
					partWithName("customerName").description("The value shouldn't be empty."),
					partWithName("companyName").description("The value can be empty."),
					partWithName("inChargeName").description("The value can be empty."),
					partWithName("email").description("The value shouldn't be empty."),
					partWithName("categoryName").description("The value shouldn't be empty."),
					partWithName("phoneNumber").description("The value shouldn't be empty."),
					partWithName("address").description("The value can be empty."),
					partWithName("detailAddress").description("The value can be empty."),
					partWithName("memo").description("The value can be empty."),
					partWithName("estimateContent").description("The value can be empty."),
					partWithName("password").description("The value shouldn't be empty."),
					partWithName("paymentMethod").description("The value should be one of [CASH, CREDIT_CARD]."),
					partWithName("files").description("The value can be empty. Each file's max size is 5MB."),
					partWithName("price").description("The value should be greater or equal than 0."),
					partWithName("draftStatus").description("The value should be one of [HOLDING, WORKING, CHECK_REQUEST, PRINT_REQUEST, COMPLETED].")
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
			.multiPart("customerName", "jin")
			.multiPart("companyName", "my company")
			.multiPart("inChargeName", "hak")
			.multiPart("email", "jin@test.com")
			.multiPart("categoryName", "봉투")
			.multiPart("phoneNumber", "010-0000-0000")
			.multiPart("address", "my address")
			.multiPart("detailAddress", "my detail address")
			.multiPart("memo", "my memo")
			.multiPart("estimateContent", "my estimate content")
			.multiPart("password", "my password")
			.multiPart("paymentMethod", PaymentMethod.CREDIT_CARD.toString())
			.multiPart("price", 3000)
			.multiPart("draftStatus", DraftStatus.COMPLETED.toString())
			.put(SINGULAR + "/{id}", draft.getId());

		// then
		response.then()
			.statusCode(HttpStatus.OK.value())
			.body("data", is(Integer.valueOf(String.valueOf(draft.getId()))));
		final OK<String> ok = objectMapper.readValue(response.getBody().asString(), new TypeReference<>(){});
		final long draftId = Long.parseLong(ok.getData());
		final Path path = Path.of(DRAFT_WORK.buildPathFrom(DRAFT.buildPathFrom(testStorage.getRoot(), draftId), draftId));
		assertThat(Objects.requireNonNull(path.toFile().listFiles()).length).isEqualTo(1);
	}

}
