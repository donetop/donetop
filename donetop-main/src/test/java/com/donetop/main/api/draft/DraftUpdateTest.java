package com.donetop.main.api.draft;

import com.donetop.domain.entity.draft.Draft;
import com.donetop.enums.draft.DraftStatus;
import com.donetop.enums.payment.PaymentMethod;
import com.donetop.main.api.common.IntegrationBase;
import com.donetop.repository.draft.DraftRepository;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static com.donetop.main.api.draft.DraftAPIController.PATH.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DraftUpdateTest extends IntegrationBase {

	@Autowired
	private DraftRepository draftRepository;

	@AfterAll
	void afterAll() {
		draftRepository.deleteAll();
	}

	@Test
	void updateOne_withInvalidFieldValues_return400() throws Exception {
		// given
		final JSONObject body = new JSONObject()
			.put("customerName", "")
			.put("draftStatus", JSONObject.NULL)
			.put("address", JSONObject.NULL)
			.put("price", 100)
			.put("paymentMethod", JSONObject.NULL)
			.put("memo", JSONObject.NULL)
			.put("password", "");

		// when
		final ResultActions resultActions = mockMvc.perform(
				put(SINGULAR + "/{id}", 1)
					.contentType(MediaType.APPLICATION_JSON)
					.content(body.toString())
			)
			.andDo(print())
		;

		// then
		resultActions
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.reason", hasSize(7)))
			.andDo(
				document(
					"draft_update/updateOne_withInvalidFieldValues_return400",
					preprocessRequest(),
					preprocessResponse()
				)
			)
		;
	}

	@Test
	void updateOne_withUnknownId_return400() throws Exception {
	    // given
		final JSONObject body = new JSONObject()
			.put("customerName", "mun")
			.put("draftStatus", DraftStatus.COMPLETED)
			.put("address", "new address")
			.put("price", 3000)
			.put("paymentMethod", PaymentMethod.CHECK_CARD)
			.put("memo", "new memo")
			.put("password", "new password");

		// when
		final ResultActions resultActions = mockMvc.perform(
				put(SINGULAR + "/{id}", 100)
					.contentType(MediaType.APPLICATION_JSON)
					.content(body.toString())
			)
			.andDo(print())
		;

		// then
		resultActions
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.reason", containsString("존재하지 않는 시안입니다.")))
			.andDo(
				document(
					"draft_update/updateOne_withUnknownId_return400",
					preprocessRequest(),
					preprocessResponse()
				)
			)
		;
	}

	@Test
	void updateOne_withValidFieldValues_return200() throws Exception {
		// given
		Draft draft = Draft.builder()
			.customerName("jin")
			.price(2000)
			.address("address")
			.memo("memo")
			.password("password").build();
		draftRepository.save(draft);
		assert draft.getId() != 0L;
		final JSONObject body = new JSONObject()
			.put("customerName", "mun")
			.put("draftStatus", DraftStatus.COMPLETED)
			.put("address", "new address")
			.put("price", 3000)
			.put("paymentMethod", PaymentMethod.CHECK_CARD)
			.put("memo", "new memo")
			.put("password", "new password");

		// when
		final ResultActions resultActions = mockMvc.perform(
				put(SINGULAR + "/{id}", draft.getId())
					.contentType(MediaType.APPLICATION_JSON)
					.content(body.toString())
			)
			.andDo(print())
		;

		// then
		resultActions
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data", is(Integer.valueOf(String.valueOf(draft.getId())))))
			.andDo(
				document(
					"draft_update/updateOne_withValidFieldValues_return200",
					preprocessRequest(),
					preprocessResponse(),
					requestFields(
						fieldWithPath("customerName").type(STRING).description("This field shouldn't be empty."),
						fieldWithPath("price").type(NUMBER).description("This field should be greater or equal than 1000."),
						fieldWithPath("address").type(STRING).description("This field shouldn't be null."),
						fieldWithPath("memo").type(STRING).description("This field shouldn't be null."),
						fieldWithPath("password").type(STRING).description("This field shouldn't be empty."),
						fieldWithPath("draftStatus").type(STRING).description("This field shouldn't be null."),
						fieldWithPath("paymentMethod").type(STRING).description("This field shouldn't be null.")
					),
					responseFields(
						fieldWithPath("status").type(STRING).description("Status value."),
						fieldWithPath("code").type(NUMBER).description("Status code."),
						fieldWithPath("data").type(NUMBER).description("This is the original draft id.")
					)
				)
			)
		;
	}

}
