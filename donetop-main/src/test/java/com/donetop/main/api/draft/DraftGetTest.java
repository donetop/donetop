package com.donetop.main.api.draft;

import com.donetop.BaseTest;
import com.donetop.domain.entity.draft.Draft;
import com.donetop.repository.draft.DraftRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.donetop.main.api.draft.DraftAPIController.PATH.ROOT;
import static org.hamcrest.Matchers.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DraftGetTest extends BaseTest {

	@Autowired
	private DraftRepository draftRepository;

	private final List<Draft> drafts = new ArrayList<>();

	@BeforeAll
	void beforeAll() {
		for (int i = 0; i < 100; i++) {
			Draft draft = Draft.builder()
				.customerName("jin" + i)
				.price(1000 + i)
				.address("address" + i)
				.memo("memo" + i).build();
			drafts.add(draft);
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

	    // when & then
		mockMvc.perform(
				get(String.format("%s/%d", ROOT, id))
			)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.reason", containsString("존재하지 않는 시안입니다.")))
			.andDo(print())
			.andDo(
				document(
					"draft_get/getOne_withInvalidId_return400",
					preprocessRequest(prettyPrint()),
					preprocessResponse(prettyPrint())
				)
			)
		;
	}

	@Test
	void getOne_withValidId_return200() throws Exception {
		// given
		final Draft lastDraft = Objects.requireNonNull(drafts.get(drafts.size() - 1));

		// when & then
		mockMvc.perform(
				get(String.format("%s/%d", ROOT, lastDraft.getId()))
			)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.customerName", is(lastDraft.getCustomerName())))
			.andExpect(jsonPath("$.data.draftStatus", is(lastDraft.getDraftStatus().toString())))
			.andExpect(jsonPath("$.data.address", is(lastDraft.getAddress())))
			.andExpect(jsonPath("$.data.price", is(Integer.valueOf(String.valueOf(lastDraft.getPrice())))))
			.andExpect(jsonPath("$.data.paymentMethod", is(lastDraft.getPaymentMethod().toString())))
			.andExpect(jsonPath("$.data.memo", is(lastDraft.getMemo())))
			.andDo(print())
			.andDo(
				document(
					"draft_get/getOne_withValidId_return200",
					preprocessRequest(prettyPrint()),
					preprocessResponse(prettyPrint())
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
				get(ROOT).params(params)
			)
			.andExpect(status().isBadRequest())
			.andDo(print())
			.andDo(
				document(
					"draft_get/getMultiple_withInvalidPageValue_return400",
					preprocessRequest(prettyPrint()),
					preprocessResponse(prettyPrint())
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
				get(ROOT).params(params)
			)
			.andExpect(status().isBadRequest())
			.andDo(print())
			.andDo(
				document(
					"draft_get/getMultiple_withInvalidSizeValue_return400",
					preprocessRequest(prettyPrint()),
					preprocessResponse(prettyPrint())
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
				get(ROOT).params(params)
			)
			.andExpect(status().isBadRequest())
			.andDo(print())
			.andDo(
				document(
					"draft_get/getMultiple_withInvalidDirectionValue_return400",
					preprocessRequest(prettyPrint()),
					preprocessResponse(prettyPrint())
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
				get(ROOT).params(params)
			)
			.andExpect(status().isBadRequest())
			.andDo(print())
			.andDo(
				document(
					"draft_get/getMultiple_withInvalidPropertyValue_return400",
					preprocessRequest(prettyPrint()),
					preprocessResponse(prettyPrint())
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
		params.add("property", "id");

		// when & then
		mockMvc.perform(
				get(ROOT).params(params)
			)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.content", hasSize(5)))
			.andExpect(jsonPath("$.data.first", is(false)))
			.andExpect(jsonPath("$.data.numberOfElements", is(5)))
			.andExpect(jsonPath("$.data.number", is(1)))
			.andDo(print())
			.andDo(
				document(
					"draft_get/getMultiple_withValidParams_return200",
					preprocessRequest(prettyPrint()),
					preprocessResponse(prettyPrint()),
					requestParameters(
						parameterWithName("page").description("The page to retrieve. Default is 0."),
						parameterWithName("size").description("The size to retrieve. Default is 20."),
						parameterWithName("property").description("The property for sorting. Default is \"createTime\"."),
						parameterWithName("direction").description("The direction of property. Default is \"desc\".")
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
				get(ROOT)
			)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.content", hasSize(20)))
			.andExpect(jsonPath("$.data.first", is(true)))
			.andDo(print())
			.andDo(
				document(
					"draft_get/getMultiple_withoutParams_return200",
					preprocessRequest(prettyPrint()),
					preprocessResponse(prettyPrint())
				)
			)
		;
	}
}
