package com.donetop.main.api.draft;

import com.donetop.domain.entity.draft.Draft;
import com.donetop.main.api.common.IntegrationBase;
import com.donetop.repository.draft.DraftRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.donetop.main.api.draft.DraftAPIController.PATH.PLURAL;
import static com.donetop.main.api.draft.DraftAPIController.PATH.SINGULAR;
import static org.hamcrest.Matchers.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DraftGetTest extends IntegrationBase {

	@Autowired
	private DraftRepository draftRepository;

	private final List<Draft> drafts = new ArrayList<>();

	@BeforeAll
	void beforeAll() {
		LocalDateTime now = LocalDateTime.now();
		for (int i = 0; i < 100; i++) {
			Draft draft = Draft.testBuilder()
				.customerName("jin" + i)
				.price(1000 + i)
				.address("address" + i)
				.memo("memo" + i)
				.password("password" + i)
				.createTime(now)
				.updateTime(now).build();
			drafts.add(draft);
			now = now.plusDays(1L);
		}
		draftRepository.saveAll(drafts);
	}

	@AfterAll
	void afterAll() {
		draftRepository.deleteAll();
	}

	@Test
	void getOne_withInvalidId_return400() throws Exception {
	    // given
		final long id = 1000;

	    // when
		final ResultActions resultActions = mockMvc.perform(
				get(SINGULAR + "/{id}", id)
			)
			.andDo(print())
		;

		// then
		resultActions
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.reason", containsString("존재하지 않는 시안입니다.")))
			.andDo(
				document(
					"draft_get/getOne_withInvalidId_return400",
					preprocessRequest(),
					preprocessResponse()
				)
			)
		;
	}

	@Test
	void getOne_withValidId_return200() throws Exception {
		// given
		final Draft draft = Objects.requireNonNull(drafts.get(51));

		// when
		final ResultActions resultActions = mockMvc.perform(
				get(SINGULAR + "/{id}", draft.getId())
			)
			.andDo(print())
		;

		// then
		resultActions
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.customerName", is(draft.getCustomerName())))
			.andExpect(jsonPath("$.data.draftStatus", is(draft.getDraftStatus().toString())))
			.andExpect(jsonPath("$.data.address", is(draft.getAddress())))
			.andExpect(jsonPath("$.data.price", is(Integer.valueOf(String.valueOf(draft.getPrice())))))
			.andExpect(jsonPath("$.data.paymentMethod", is(draft.getPaymentMethod().toString())))
			.andExpect(jsonPath("$.data.memo", is(draft.getMemo())))
			.andDo(
				document(
					"draft_get/getOne_withValidId_return200",
					preprocessRequest(),
					preprocessResponse(),
					responseFields(
						fieldWithPath("status").type(STRING).description("Status value."),
						fieldWithPath("code").type(NUMBER).description("Status code."),
						fieldWithPath("data").type(OBJECT).description("Response data."),
						fieldWithPath("data.id").type(NUMBER).description("Draft id."),
						fieldWithPath("data.customerName").description("Draft customerName."),
						fieldWithPath("data.draftStatus").type(STRING).description("Draft status."),
						fieldWithPath("data.address").type(STRING).description("Draft address."),
						fieldWithPath("data.price").description("Draft price."),
						fieldWithPath("data.paymentMethod").type(STRING).description("Draft paymentMethod."),
						fieldWithPath("data.memo").type(STRING).description("Draft memo."),
						fieldWithPath("data.createTime").type(STRING).description("Draft create time."),
						fieldWithPath("data.updateTime").type(STRING).description("Draft update time.")
					)
				)
			)
		;
	}

	@Test
	void getMultiple_withInvalidPageValue_return400() throws Exception {
		// given
		final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("page", "-1");

		// when & then
		mockMvc.perform(
				get(PLURAL).params(params)
			)
			.andExpect(status().isBadRequest())
			.andDo(print())
			.andDo(
				document(
					"draft_get/getMultiple_withInvalidPageValue_return400",
					preprocessRequest(),
					preprocessResponse()
				)
			)
		;
	}

	@Test
	void getMultiple_withInvalidSizeValue_return400() throws Exception {
		// given
		final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("size", "-1");

		// when & then
		mockMvc.perform(
				get(PLURAL).params(params)
			)
			.andExpect(status().isBadRequest())
			.andDo(print())
			.andDo(
				document(
					"draft_get/getMultiple_withInvalidSizeValue_return400",
					preprocessRequest(),
					preprocessResponse()
				)
			)
		;
	}

	@Test
	void getMultiple_withInvalidDirectionValue_return400() throws Exception {
		// given
		final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("direction", "adesc");

		// when & then
		mockMvc.perform(
				get(PLURAL).params(params)
			)
			.andExpect(status().isBadRequest())
			.andDo(print())
			.andDo(
				document(
					"draft_get/getMultiple_withInvalidDirectionValue_return400",
					preprocessRequest(),
					preprocessResponse()
				)
			)
		;
	}

	@Test
	void getMultiple_withInvalidPropertyValue_return400() throws Exception {
		// given
		final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("property", "iddd");

		// when & then
		mockMvc.perform(
				get(PLURAL).params(params)
			)
			.andExpect(status().isBadRequest())
			.andDo(print())
			.andDo(
				document(
					"draft_get/getMultiple_withInvalidPropertyValue_return400",
					preprocessRequest(),
					preprocessResponse()
				)
			)
		;
	}

	@Test
	void getMultiple_withValidParams_return200() throws Exception {
		// given
		final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("page", "1");
		params.add("size", "5");
		params.add("direction", "desc");
		params.add("property", "price");

		// when
		final ResultActions resultActions = mockMvc.perform(
				get(PLURAL).params(params)
			)
			.andDo(print())
		;

		// then
		resultActions
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.content", hasSize(5)))
			.andExpect(jsonPath("$.data.first", is(false)))
			.andExpect(jsonPath("$.data.totalPages", is(20)))
			.andExpect(jsonPath("$.data.totalElements", is(100)))
			.andDo(
				document(
					"draft_get/getMultiple_withValidParams_return200",
					preprocessRequest(),
					preprocessResponse(),
					requestParameters(
						parameterWithName("page").description("The page to retrieve. Default is 0."),
						parameterWithName("size").description("The size to retrieve. Default is 20."),
						parameterWithName("property").description("The property for sorting. Default is \"createTime\"."),
						parameterWithName("direction").description("The direction of property. Default is \"desc\".")
					),
					responseFields(
						fieldWithPath("status").type(STRING).description("Status value."),
						fieldWithPath("code").type(NUMBER).description("Status code."),
						fieldWithPath("data").type(OBJECT).description("Response data."),
						subsectionWithPath("data.content").type(ARRAY).description("Searched Drafts. See the Get One Draft response body field description for details."),
						subsectionWithPath("data.pageable").type(OBJECT).description("Pageable information."),
						fieldWithPath("data.last").type(BOOLEAN).description("Last information."),
						fieldWithPath("data.totalElements").type(NUMBER).description("TotalElements information."),
						fieldWithPath("data.totalPages").type(NUMBER).description("TotalPages information."),
						fieldWithPath("data.size").type(NUMBER).description("Size information."),
						fieldWithPath("data.number").type(NUMBER).description("Number information."),
						subsectionWithPath("data.sort").type(OBJECT).description("Sort information."),
						fieldWithPath("data.first").type(BOOLEAN).description("First information."),
						fieldWithPath("data.numberOfElements").type(NUMBER).description("NumberOfElements information."),
						fieldWithPath("data.empty").type(BOOLEAN).description("Empty information.")
					)
				)
			)
		;
	}

	@Test
	void getMultiple_withoutParams_return200() throws Exception {
		// given

		// when & then
		mockMvc.perform(
				get(PLURAL)
			)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.content", hasSize(20)))
			.andExpect(jsonPath("$.data.first", is(true)))
			.andExpect(jsonPath("$.data.totalPages", is(5)))
			.andExpect(jsonPath("$.data.totalElements", is(100)))
			.andDo(print())
			.andDo(
				document(
					"draft_get/getMultiple_withoutParams_return200",
					preprocessRequest(),
					preprocessResponse()
				)
			)
		;
	}
}
