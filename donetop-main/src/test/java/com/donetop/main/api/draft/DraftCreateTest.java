package com.donetop.main.api.draft;

import com.donetop.BaseTest;
import com.donetop.repository.draft.DraftRepository;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static com.donetop.main.api.draft.DraftAPIController.PATH.*;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DraftCreateTest extends BaseTest {

	@Autowired
	private DraftRepository draftRepository;

	@AfterAll
	void afterAll() {
		draftRepository.deleteAll();
	}

	@Test
	void create_withEmptyBody_return400() throws Exception {
		// given
		final JSONObject body = new JSONObject();

		// when & then
		mockMvc.perform(
				post(ROOT)
					.contentType(MediaType.APPLICATION_JSON)
					.content(body.toString())
			)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.reason", hasSize(4)))
			.andDo(print())
			.andDo(
				document(
					"draft_create/create_withEmptyBody_return400",
					preprocessRequest(prettyPrint()),
					preprocessResponse(prettyPrint())
				)
			)
		;
	}

	@Test
	void create_withInvalidFieldValues_return400() throws Exception {
		// given
		final JSONObject body = new JSONObject()
			.put("customerName", "")
			.put("address", JSONObject.NULL)
			.put("price", 0)
			.put("memo", JSONObject.NULL);

		// when & then
		mockMvc.perform(
				post(ROOT)
					.contentType(MediaType.APPLICATION_JSON)
					.content(body.toString())
			)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.reason", hasSize(4)))
			.andDo(print())
			.andDo(
				document(
					"draft_create/create_withInvalidFieldValues_return400",
					preprocessRequest(prettyPrint()),
					preprocessResponse(prettyPrint())
				)
			)
		;
	}

	@Test
	void create_withValidFieldValues_return200() throws Exception {
		// given
		final JSONObject body = new JSONObject()
			.put("customerName", "jin")
			.put("address", "my address")
			.put("price", 1000)
			.put("memo", "simple test");

		// when & then
		mockMvc.perform(
				post(ROOT)
					.contentType(MediaType.APPLICATION_JSON)
					.content(body.toString())
			)
			.andExpect(status().isOk())
			.andDo(print())
			.andDo(
				document(
					"draft_create/create_withValidFieldValues_return200",
					preprocessRequest(prettyPrint()),
					preprocessResponse(prettyPrint()),
					requestFields(
						fieldWithPath("customerName").type(STRING).description("This field shouldn't be empty."),
						fieldWithPath("price").type(NUMBER).description("This field should be greater or equal than 1000."),
						fieldWithPath("address").type(STRING).description("This field shouldn't be null."),
						fieldWithPath("memo").type(STRING).description("This field shouldn't be null.")
					),
					responseFields(
						fieldWithPath("status").type(STRING).description("Status value."),
						fieldWithPath("code").type(NUMBER).description("Status code."),
						fieldWithPath("data").type(OBJECT).description("Response data."),
						fieldWithPath("data.id").type(NUMBER).description("This is auto generated id by server."),
						fieldWithPath("data.customerName").description("The value that is sent by client."),
						fieldWithPath("data.draftStatus").type(STRING).description("Default value is \"HOLDING\"."),
						fieldWithPath("data.address").type(STRING).description("The value that is sent by client."),
						fieldWithPath("data.price").description("The value that is sent by client."),
						fieldWithPath("data.paymentMethod").type(STRING).description("Default value is \"CASH\"."),
						fieldWithPath("data.memo").type(STRING).description("The value that is sent by client."),
						fieldWithPath("data.createTime").type(STRING).description("Draft create time."),
						fieldWithPath("data.updateTime").type(STRING).description("Draft update time.")
					)
				)
			)
		;
	}

}
