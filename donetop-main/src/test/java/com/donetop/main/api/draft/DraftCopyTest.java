package com.donetop.main.api.draft;

import com.donetop.common.api.Response.OK;
import com.donetop.common.service.storage.LocalFileUtil;
import com.donetop.domain.entity.draft.Draft;
import com.donetop.main.api.common.DraftBase;
import com.fasterxml.jackson.core.type.TypeReference;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import static com.donetop.common.api.Message.UNKNOWN_DRAFT;
import static com.donetop.enums.folder.DomainType.DRAFT;
import static com.donetop.enums.folder.FolderType.DRAFT_ORDER;
import static com.donetop.enums.folder.FolderType.DRAFT_WORK;
import static com.donetop.main.api.draft.DraftAPIController.URI.COPY;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class DraftCopyTest extends DraftBase {

	@Test
	void copy_withInvalidId_return400() throws Exception {
		// given
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"draft_copy/copy_withInvalidId_return400"
			)
		);
		JSONObject body = new JSONObject().put("id", -1);

		// when
		final Response response = given.when()
			.contentType(ContentType.JSON)
			.body(body.toString())
			.post(COPY);

		// then
		response.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("reason", hasSize(1));
	}

	@Test
	void copy_withUnknownId_return400() throws Exception {
		// given
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"draft_copy/copy_withUnknownId_return400"
			)
		);
		JSONObject body = new JSONObject().put("id", 2000);

		// when
		final Response response = given.when()
			.contentType(ContentType.JSON)
			.body(body.toString())
			.post(COPY);

		// then
		response.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("reason", containsString(UNKNOWN_DRAFT));
	}

	@Test
	void copy_noFolderDraft_return200() throws Exception {
		// given
		final Draft draft = saveSingleDraftWithoutFiles();
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"draft_copy/copy_noFolderDraft_return200"
			)
		);
		JSONObject body = new JSONObject().put("id", draft.getId());

		// when
		final Response response = given.when()
			.contentType(ContentType.JSON)
			.body(body.toString())
			.post(COPY);

		// then
		response.then()
			.statusCode(HttpStatus.OK.value())
			.body("data", not(draft.getId()));
	}

	@Test
	void copy_hasFolderDraft_return200() throws Exception {
		// given
		final List<File> files = LocalFileUtil.readFiles(Path.of(testStorage.getSrc()));
		final Draft draft = saveSingleDraftWithFiles(DRAFT_ORDER, DRAFT_WORK);
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"draft_copy/copy_hasFolderDraft_return200",
				requestFields(
					fieldWithPath("id").description("The draft id for copying.")
				),
				responseFields(
					fieldWithPath("status").type(STRING).description("Status value."),
					fieldWithPath("code").type(NUMBER).description("Status code."),
					fieldWithPath("data").type(NUMBER).description("This is the copied draft id.")
				)
			)
		);
		JSONObject body = new JSONObject().put("id", draft.getId());

		// when
		final Response response = given.when()
			.contentType(ContentType.JSON)
			.body(body.toString())
			.post(COPY);

		// then
		response.then()
			.statusCode(HttpStatus.OK.value())
			.body("data", not(draft.getId()));
		final OK<String> ok = objectMapper.readValue(response.getBody().asString(), new TypeReference<>(){});
		final long copiedDraftId = Long.parseLong(ok.getData());
		final Path orderPath = Path.of(DRAFT_ORDER.buildPathFrom(DRAFT.buildPathFrom(testStorage.getRoot(), copiedDraftId), copiedDraftId));
		final Path workPath = Path.of(DRAFT_WORK.buildPathFrom(DRAFT.buildPathFrom(testStorage.getRoot(), copiedDraftId), copiedDraftId));
		assertThat(orderPath.toFile().exists()).isFalse();
		assertThat(workPath.toFile().exists()).isTrue();
		assertThat(Objects.requireNonNull(workPath.toFile().listFiles()).length).isEqualTo(files.size());
	}

}
