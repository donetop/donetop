package com.donetop.main.api.draft;

import com.donetop.common.service.storage.LocalFileUtil;
import com.donetop.domain.entity.draft.Draft;
import com.donetop.domain.entity.user.User;
import com.donetop.enums.user.RoleType;
import com.donetop.main.api.common.DraftBase;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static com.donetop.enums.folder.FolderType.DRAFT_ORDER;
import static com.donetop.enums.folder.FolderType.DRAFT_WORK;
import static com.donetop.main.api.draft.DraftAPIController.URI.SINGULAR;
import static org.hamcrest.Matchers.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class DraftSingleGetTest extends DraftBase {

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
		final Draft draft = saveSingleDraftWithoutFiles();
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
		final Draft draft = saveSingleDraftWithoutFiles();
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
			.body("data.draftStatus.name", is(draft.getDraftStatus().name()))
			.body("data.categoryName", is(draft.getCategoryName()))
			.body("data.address", is(draft.getAddress()))
			.body("data.detailAddress", is(draft.getDetailAddress()))
			.body("data.price", is(Integer.valueOf(String.valueOf(draft.getPrice()))))
			.body("data.paymentMethod.name", is(draft.getPaymentMethod().name()))
			.body("data.memo", is(draft.getMemo()));
	}

	@Test
	void getSingleByAdmin_withValidId_return200() throws Exception {
		// given
		final Draft draft = saveSingleDraftWithoutFiles();
		final User admin = saveUser("admin", RoleType.ADMIN);
		final Map<String, String> cookies = doLoginWith(admin).cookies();
		final RequestSpecification given = RestAssured.given(this.spec);
		given.filter(
			document(
				"draft_single_get/getSingleByAdmin_withValidId_return200"
			)
		);

		// when
		final Response response = given.when()
			.cookies(cookies)
			.get(SINGULAR + "/{id}", draft.getId());

		// then
		response.then()
			.statusCode(HttpStatus.OK.value())
			.body("data.customerName", is(draft.getCustomerName()))
			.body("data.draftStatus.name", is(draft.getDraftStatus().name()))
			.body("data.categoryName", is(draft.getCategoryName()))
			.body("data.address", is(draft.getAddress()))
			.body("data.detailAddress", is(draft.getDetailAddress()))
			.body("data.price", is(Integer.valueOf(String.valueOf(draft.getPrice()))))
			.body("data.paymentMethod.name", is(draft.getPaymentMethod().name()))
			.body("data.memo", is(draft.getMemo()));
	}

	@Test
	void getSingleThatHasFolder_withValidIdAndRightPassword_return200() {
		// given
		final List<File> files = LocalFileUtil.readFiles(Path.of(testStorage.getSrc()));
		final Draft draft = saveSingleDraftWithFiles(DRAFT_ORDER, DRAFT_WORK);
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
					fieldWithPath("data.customerName").type(STRING).description("Draft customerName."),
					fieldWithPath("data.companyName").type(STRING).description("Draft companyName."),
					fieldWithPath("data.inChargeName").type(STRING).description("Draft inChargeName."),
					fieldWithPath("data.email").type(STRING).description("Draft email."),
					fieldWithPath("data.phoneNumber").type(STRING).description("Draft phoneNumber."),
					fieldWithPath("data.categoryName").type(STRING).description("Draft categoryName."),
					fieldWithPath("data.draftStatus").type(OBJECT).description("Draft status."),
					fieldWithPath("data.draftStatus.name").type(STRING).description("Draft status name."),
					fieldWithPath("data.draftStatus.value").type(STRING).description("Draft status value."),
					fieldWithPath("data.address").type(STRING).description("Draft address."),
					fieldWithPath("data.detailAddress").type(STRING).description("Draft detailAddress."),
					fieldWithPath("data.price").type(NUMBER).description("Draft price."),
					fieldWithPath("data.paymentMethod").type(OBJECT).description("Draft paymentMethod."),
					fieldWithPath("data.paymentMethod.name").type(STRING).description("Draft paymentMethod name."),
					fieldWithPath("data.paymentMethod.value").type(STRING).description("Draft paymentMethod value."),
					fieldWithPath("data.memo").type(STRING).description("Draft memo."),
					fieldWithPath("data.createTime").type(STRING).description("Draft create time."),
					fieldWithPath("data.updateTime").type(STRING).description("Draft update time."),
					subsectionWithPath("data.folders").type(ARRAY).description("Draft folders."),
					fieldWithPath("data.folders[].folderType").type(STRING).description("Draft folderType."),
					subsectionWithPath("data.folders[].files").type(ARRAY).description("The files that are included in a folder."),
					subsectionWithPath("data.comments").type(ARRAY).description("Draft comments.")
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
			.body("data.draftStatus.name", is(draft.getDraftStatus().name()))
			.body("data.categoryName", is(draft.getCategoryName()))
			.body("data.address", is(draft.getAddress()))
			.body("data.detailAddress", is(draft.getDetailAddress()))
			.body("data.price", is(Integer.valueOf(String.valueOf(draft.getPrice()))))
			.body("data.paymentMethod.name", is(draft.getPaymentMethod().name()))
			.body("data.memo", is(draft.getMemo()))
			.body("data.folders", hasSize(2))
			.body("data.folders[0].files", hasSize(files.size()))
			.body("data.folders[1].files", hasSize(files.size()));
	}
}
