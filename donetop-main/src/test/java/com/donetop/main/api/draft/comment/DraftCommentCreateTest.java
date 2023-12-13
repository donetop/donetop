package com.donetop.main.api.draft.comment;

import com.donetop.common.api.Response.OK;
import com.donetop.common.service.storage.LocalFileUtil;
import com.donetop.domain.entity.draft.Draft;
import com.donetop.main.api.common.DraftCommentBase;
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

import static com.donetop.common.api.Message.FILE_SIZE_EXCEED;
import static com.donetop.enums.folder.DomainType.DRAFT_COMMENT;
import static com.donetop.main.api.draft.DraftCommentAPIController.URI.SINGULAR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class DraftCommentCreateTest extends DraftCommentBase {

	@Test
	void create_withoutParts_return400() {
		// given
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"draft_comment_create/create_withoutParts_return400"
			)
		);

		// when
		final Response response = given.when()
			.post(SINGULAR);

		// then
		response.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("reason", hasSize(2));
	}

	@Test
	void create_withInvalidPartValues_return400() {
		// given
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"draft_comment_create/create_withInvalidPartValues_return400"
			)
		);

		// when
		final Response response = given.when()
			.multiPart("content", "")
			.multiPart("draftId", -1)
			.post(SINGULAR);

		// then
		response.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("reason", hasSize(2));
	}

	@Test
	void create_withValidPartValues_return200() {
		// given
		final Draft draft = saveSingleDraftWithoutFiles();
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"draft_comment_create/create_withValidPartValues_return200"
			)
		);

		// when
		final Response response = given.when()
			.multiPart("content", "my content")
			.multiPart("draftId", draft.getId())
			.post(SINGULAR);

		// then
		response.then()
			.statusCode(HttpStatus.OK.value());
	}

	@Test
	void create_withValidPartValuesAndSizeExceedFiles_return400() {
		// given
		final Draft draft = saveSingleDraftWithoutFiles();
		final List<File> files = LocalFileUtil.readFiles(Path.of(testStorage.getSrc()));
		final RequestSpecification given = RestAssured.given(this.spec);
		for (final File file : files) given.multiPart("files", file);
		given.filter(
			document(
				"draft_comment_create/create_withValidPartValuesAndSizeExceedFiles_return400"
			)
		);

		// when
		final Response response = given.when()
			.multiPart("content", "my content")
			.multiPart("draftId", draft.getId())
			.post(SINGULAR);

		// then
		response.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("reason", is(FILE_SIZE_EXCEED));
	}

	@Test
	void create_withValidPartValuesAndFiles_return200() throws Exception {
		// given
		final int subSize = 1;
		final Draft draft = saveSingleDraftWithoutFiles();
		final List<File> files = LocalFileUtil.readFiles(Path.of(testStorage.getSrc())).subList(0, subSize);
		final RequestSpecification given = RestAssured.given(this.spec);
		for (final File file : files) given.multiPart("files", file);
		given.filter(
			document(
				"draft_comment_create/create_withValidPartValuesAndFiles_return200",
				requestParts(
					partWithName("content").description("The value shouldn't be empty."),
					partWithName("draftId").description("The value should be greater than 0."),
					partWithName("files").description("The value can be empty. Each file's max size is 5MB.")
				),
				responseFields(
					fieldWithPath("status").type(STRING).description("Status value."),
					fieldWithPath("code").type(NUMBER).description("Status code."),
					fieldWithPath("data").type(NUMBER).description("This is the auto generated comment id.")
				)
			)
		);

		// when
		final Response response = given.when()
			.multiPart("content", "my content")
			.multiPart("draftId", draft.getId())
			.post(SINGULAR);

		// then
		response.then().statusCode(HttpStatus.OK.value());
		final OK<String> ok = objectMapper.readValue(response.getBody().asString(), new TypeReference<>(){});
		final long commentId = Long.parseLong(ok.getData());
		Path path = Path.of(DRAFT_COMMENT.buildPathFrom(testStorage.getRoot(), commentId));
		assertThat(Objects.requireNonNull(path.toFile().listFiles()).length).isEqualTo(subSize);
	}

}
