package com.donetop.main.api.draft;

import com.donetop.domain.entity.draft.Draft;
import com.donetop.domain.entity.user.User;
import com.donetop.enums.folder.FolderType;
import com.donetop.enums.user.RoleType;
import com.donetop.main.api.common.DraftBase;
import com.donetop.common.api.Response.OK;
import com.fasterxml.jackson.core.type.TypeReference;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.nio.file.Path;

import static com.donetop.main.api.draft.DraftAPIController.URI.SINGULAR;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class DraftSingleDeleteTest extends DraftBase {

	private User jin;

	private User admin;

	@BeforeAll
	void beforeAll() {
		jin = saveUser("jin", RoleType.NORMAL);
		admin = saveUser("admin", RoleType.ADMIN);
	}

	@Test
	void deleteSingle_withoutSession_return400() {
		// given
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"draft_single_delete/deleteSingle_withoutSession_return400"
			)
		);

		// when
		final Response response = given.when()
			.delete(SINGULAR + "/{id}", -1);

		// then
		response.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("reason", is("유효한 세션 정보가 없습니다."));
	}

	@Test
	void deleteSingle_withUnknownId_return400() throws Exception {
		// given
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"draft_single_delete/deleteSingle_withUnknownId_return400"
			)
		);

		// when
		final Response response = given.when()
			.cookies(doLoginWith(jin).cookies())
			.delete(SINGULAR + "/{id}", -1);

		// then
		response.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("reason", containsString("존재하지 않는 시안입니다."));
	}

	@Test
	void deleteSingle_byNormalUser_return400() throws Exception {
		// given
		final Draft draft = saveSingleDraftWithoutFiles();
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"draft_single_delete/deleteSingle_byNormalUser_return400"
			)
		);

		// when
		final Response response = given.when()
			.cookies(doLoginWith(jin).cookies())
			.delete(SINGULAR + "/{id}", draft.getId());

		// then
		response.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("reason", containsString("허용되지 않은 요청입니다."));
	}

	@Test
	void deleteSingle_byAdminUser_return200() throws Exception {
		// given
		final Draft draft = saveSingleDraftWithoutFiles();
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"draft_single_delete/deleteSingle_byAdminUser_return200",
				responseFields(
					fieldWithPath("status").type(STRING).description("Status value."),
					fieldWithPath("code").type(NUMBER).description("Status code."),
					fieldWithPath("data").type(NUMBER).description("This is draft id.")
				)
			)
		);

		// when
		final Response response = given.when()
			.cookies(doLoginWith(admin).cookies())
			.delete(SINGULAR + "/{id}", draft.getId());

		// then
		response.then()
			.statusCode(HttpStatus.OK.value())
			.body("data", is(Integer.valueOf(String.valueOf(draft.getId()))));
	}

	@Test
	void deleteSingleThatHasFiles_byAdminUser_return200() throws Exception {
		// given
		final Draft draft = saveSingleDraftWithFiles();
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"draft_single_delete/deleteSingleThatHasFiles_byAdminUser_return200"
			)
		);

		// when
		final Response response = given.when()
			.cookies(doLoginWith(admin).cookies())
			.delete(SINGULAR + "/{id}", draft.getId());

		// then
		response.then()
			.statusCode(HttpStatus.OK.value())
			.body("data", is(Integer.valueOf(String.valueOf(draft.getId()))));
		final OK<String> ok = objectMapper.readValue(response.getBody().asString(), new TypeReference<>(){});
		final long draftId = Long.parseLong(ok.getData());
		final Path path = Path.of(FolderType.DRAFT.buildPathFrom(testStorage.getRoot(), draftId));
		assertThat(path.toFile().exists()).isFalse();
	}

}
