package com.donetop.main.api.draft;

import com.donetop.BaseTest;
import com.donetop.domain.entity.draft.Draft;
import com.donetop.enums.draft.DraftStatus;
import com.donetop.enums.payment.PaymentMethod;
import com.donetop.repository.draft.DraftRepository;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static com.donetop.main.api.draft.DraftAPIController.PATH.ROOT;
import static org.hamcrest.Matchers.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DraftUpdateTest extends BaseTest {

	@Autowired
	private DraftRepository draftRepository;

	@AfterAll
	void afterAll() {
		draftRepository.deleteAll();
	}

	@Test
	void update_withInvalidFieldValues_return400() throws Exception {
		// given
		final JSONObject body = new JSONObject()
			.put("id", -1)
			.put("customerName", "")
			.put("draftStatus", JSONObject.NULL)
			.put("address", JSONObject.NULL)
			.put("price", 100)
			.put("paymentMethod", JSONObject.NULL)
			.put("memo", JSONObject.NULL);

		// when & then
		mockMvc.perform(
				put(ROOT)
					.contentType(MediaType.APPLICATION_JSON)
					.content(body.toString())
			)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.reason", hasSize(7)))
			.andDo(print())
			.andDo(
				document(
					"draft_update/update_withInvalidFieldValues_return400",
					preprocessRequest(prettyPrint()),
					preprocessResponse(prettyPrint())
				)
			)
		;
	}

	@Test
	void update_withUnknownId_return400() throws Exception {
	    // given
		final JSONObject body = new JSONObject()
			.put("id", 1)
			.put("customerName", "mun")
			.put("draftStatus", DraftStatus.COMPLETED)
			.put("address", "new address")
			.put("price", 3000)
			.put("paymentMethod", PaymentMethod.CHECK_CARD)
			.put("memo", "new memo");

		// when & then
		mockMvc.perform(
				put(ROOT)
					.contentType(MediaType.APPLICATION_JSON)
					.content(body.toString())
			)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.reason", containsString("존재하지 않는 시안입니다.")))
			.andDo(print())
			.andDo(
				document(
					"draft_update/update_withUnknownId_return400",
					preprocessRequest(prettyPrint()),
					preprocessResponse(prettyPrint())
				)
			)
		;
	}

	@Test
	void update_withValidFieldValues_return200() throws Exception {
		// given
		Draft draft = Draft.builder()
			.customerName("jin")
			.price(2000)
			.address("address")
			.memo("memo").build();
		draftRepository.save(draft);
		assert draft.getId() != 0L;
		final JSONObject body = new JSONObject()
			.put("id", draft.getId())
			.put("customerName", "mun")
			.put("draftStatus", DraftStatus.COMPLETED)
			.put("address", "new address")
			.put("price", 3000)
			.put("paymentMethod", PaymentMethod.CHECK_CARD)
			.put("memo", "new memo");

		// when & then
		mockMvc.perform(
				put(ROOT)
					.contentType(MediaType.APPLICATION_JSON)
					.content(body.toString())
			)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.customerName", not(draft.getCustomerName())))
			.andExpect(jsonPath("$.data.draftStatus", not(draft.getDraftStatus())))
			.andExpect(jsonPath("$.data.address", not(draft.getAddress())))
			.andExpect(jsonPath("$.data.price", not(draft.getPrice())))
			.andExpect(jsonPath("$.data.paymentMethod", not(draft.getPaymentMethod())))
			.andExpect(jsonPath("$.data.memo", not(draft.getMemo())))
			.andDo(print())
			.andDo(
				document(
					"draft_update/update_withValidFieldValues_return200",
					preprocessRequest(prettyPrint()),
					preprocessResponse(prettyPrint())
				)
			)
		;
	}

}
