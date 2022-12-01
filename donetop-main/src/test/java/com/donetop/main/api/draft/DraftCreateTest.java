package com.donetop.main.api.draft;

import com.donetop.BaseTest;
import com.donetop.main.api.draft.request.DraftCreateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static com.donetop.main.api.draft.DraftAPIController.PATH.*;
import static org.hamcrest.Matchers.is;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DraftCreateTest extends BaseTest {

	@Test
	void create_withNullCustomerName_return400() throws Exception {
		// given
		final DraftCreateRequest draftCreateRequest =
			DraftCreateRequest.builder()
				.customerName(null)
				.address("my address")
				.price(1000L)
				.memo("simple test")
				.build();

		// when & then
		mockMvc.perform(
				post(ROOT)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(draftCreateRequest))
			)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.reason[0]").exists())
			.andExpect(jsonPath("$.reason[0].defaultMessage").exists())
			.andExpect(jsonPath("$.reason[0].defaultMessage", is("Customer name shouldn't be empty.")))
			.andDo(print())
			.andDo(document(
				"create_withNullCustomerName_return400",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint())
			))
		;
	}

	@Test
	void create_withEmptyCustomerName_return400() throws Exception {
		// given
		final DraftCreateRequest draftCreateRequest =
			DraftCreateRequest.builder()
				.customerName("")
				.address("my address")
				.price(1000L)
				.memo("simple test")
				.build();

		// when & then
		mockMvc.perform(
				post(ROOT)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(draftCreateRequest))
			)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.reason[0]").exists())
			.andExpect(jsonPath("$.reason[0].defaultMessage").exists())
			.andExpect(jsonPath("$.reason[0].defaultMessage", is("Customer name shouldn't be empty.")))
			.andDo(print())
			.andDo(document(
				"create_withEmptyCustomerName_return400",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint())
			))
		;
	}

	@Test
	void create_withLessThan1000Price_return400() throws Exception {
		// given
		final DraftCreateRequest draftCreateRequest =
			DraftCreateRequest.builder()
				.customerName("jin")
				.address("my address")
				.price(999L)
				.memo("simple test")
				.build();

		// when & then
		mockMvc.perform(
				post(ROOT)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(draftCreateRequest))
			)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.reason[0]").exists())
			.andExpect(jsonPath("$.reason[0].defaultMessage").exists())
			.andExpect(jsonPath("$.reason[0].defaultMessage", is("The minimum price is 1,000.")))
			.andDo(document(
				"create_withLessThan1000Price_return400",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint())
			))
		;
	}

	@Test
	void create_withValidFields_return200() throws Exception {
		// given
		final DraftCreateRequest draftCreateRequest =
			DraftCreateRequest.builder()
				.customerName("jin")
				.address("my address")
				.price(1000L)
				.memo("simple test")
				.build();

		// when & then
		mockMvc.perform(
				post(ROOT)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(draftCreateRequest))
			)
			.andExpect(status().isOk())
			.andDo(print())
			.andDo(
				document(
					"create_withValidFields_return200",
					preprocessRequest(prettyPrint()),
					preprocessResponse(prettyPrint()),
					requestFields(
						fieldWithPath("customerName").type(STRING).description("This is essential."),
						fieldWithPath("price").type(NUMBER).description("This is essential."),
						fieldWithPath("address").type(STRING).description("This is optional and default value is empty."),
						fieldWithPath("memo").type(STRING).description("This is optional and default value is empty.")
					),
					responseFields(
						fieldWithPath("status").type(STRING).description("status value."),
						fieldWithPath("code").type(NUMBER).description("status code."),
						fieldWithPath("data").type(OBJECT).description("response data."),
						fieldWithPath("data.id").type(NUMBER).description("This is auto generated id by server."),
						fieldWithPath("data.customerName").description("The value that is sent by client."),
						fieldWithPath("data.draftStatus").type(STRING).description("Default value is \"HOLDING\"."),
						fieldWithPath("data.address").type(STRING).description("Default value is empty."),
						fieldWithPath("data.price").description("The value that is sent by client."),
						fieldWithPath("data.paymentMethod").type(STRING).description("Default value is \"CASH\"."),
						fieldWithPath("data.memo").type(STRING).description("Default value is empty."),
						fieldWithPath("data.createTime").type(STRING).description("Draft create time."),
						fieldWithPath("data.updateTime").type(STRING).description("Draft update time.")
					)
				)
			);
	}

}
