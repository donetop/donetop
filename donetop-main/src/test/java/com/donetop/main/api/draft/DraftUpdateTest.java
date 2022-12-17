package com.donetop.main.api.draft;

import com.donetop.domain.entity.draft.Draft;
import com.donetop.enums.draft.DraftStatus;
import com.donetop.enums.folder.FolderType;
import com.donetop.enums.payment.PaymentMethod;
import com.donetop.main.api.common.IntegrationBase;
import com.donetop.main.api.common.Response;
import com.donetop.main.common.TestFileUtil;
import com.donetop.repository.draft.DraftRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import static com.donetop.main.api.draft.DraftAPIController.PATH.*;
import static com.donetop.main.properties.ApplicationProperties.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DraftUpdateTest extends IntegrationBase {

	@Autowired
	private DraftRepository draftRepository;

	@AfterAll
	void afterAll() throws IOException {
		draftRepository.deleteAll();
		FileSystemUtils.deleteRecursively(Path.of(applicationProperties.getStorage().getRoot()));
	}

	@Test
	void updateOne_withInvalidParams_return400() throws Exception {
		// given
		final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("customerName", "");
		params.add("draftStatus", null);
		params.add("address", null);
		params.add("price", "100");
		params.add("paymentMethod", null);
		params.add("memo", null);
		params.add("password", "");

		// when
		final ResultActions resultActions = mockMvc.perform(
				multipart(PUT, URI.create(SINGULAR + "/1"))
					.contentType(MediaType.MULTIPART_FORM_DATA)
					.params(params)
			)
		;

		// then
		resultActions
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.reason", hasSize(7)))
			.andDo(
				document(
					"draft_update/updateOne_withInvalidParams_return400"
				)
			)
		;
	}

	@Test
	void updateOne_withValidParamsButUnknownId_return400() throws Exception {
	    // given
		final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("customerName", "mun");
		params.add("draftStatus", DraftStatus.COMPLETED.toString());
		params.add("address", "new address");
		params.add("price", "3000");
		params.add("paymentMethod", PaymentMethod.CHECK_CARD.toString());
		params.add("memo", "new memo");
		params.add("password", "new password");

		// when
		final ResultActions resultActions = mockMvc.perform(
				multipart(PUT, URI.create(SINGULAR + "/100"))
					.contentType(MediaType.MULTIPART_FORM_DATA)
					.params(params)
			)
		;

		// then
		resultActions
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.reason", containsString("존재하지 않는 시안입니다.")))
			.andDo(
				document(
					"draft_update/updateOne_withValidParamsButUnknownId_return400"
				)
			)
		;
	}

	@Test
	void updateOne_withValidParamsAndId_return200() throws Exception {
		// given
		Draft draft = Draft.builder()
			.customerName("jin")
			.price(2000)
			.address("address")
			.memo("memo")
			.password("password").build();
		draftRepository.save(draft);
		assert draft.getId() != 0L;
		final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("customerName", "mun");
		params.add("draftStatus", DraftStatus.COMPLETED.toString());
		params.add("address", "new address");
		params.add("price", "3000");
		params.add("paymentMethod", PaymentMethod.CHECK_CARD.toString());
		params.add("memo", "new memo");
		params.add("password", "new password");

		// when
		final ResultActions resultActions = mockMvc.perform(
				multipart(PUT, URI.create(SINGULAR + "/" + draft.getId()))
					.contentType(MediaType.MULTIPART_FORM_DATA)
					.params(params)
			)
		;

		// then
		resultActions
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data", is(Integer.valueOf(String.valueOf(draft.getId())))))
			.andDo(
				document(
					"draft_update/updateOne_withValidParamsAndId_return200"
				)
			)
		;
	}

	@Test
	void updateOne_withValidParamsAndFilesAndId_return200() throws Exception {
		// given
		Draft draft = Draft.builder()
			.customerName("jin")
			.price(2000)
			.address("address")
			.memo("memo")
			.password("password").build();
		draftRepository.save(draft);
		assert draft.getId() != 0L;
		final Storage storage = applicationProperties.getStorage();
		final List<MockMultipartFile> mockMultipartFiles = TestFileUtil.readMultipartFiles(Path.of(storage.getSrc()));
		final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("customerName", "mun");
		params.add("draftStatus", DraftStatus.COMPLETED.toString());
		params.add("address", "new address");
		params.add("price", "3000");
		params.add("paymentMethod", PaymentMethod.CHECK_CARD.toString());
		params.add("memo", "new memo");
		params.add("password", "new password");

		// when
		MockMultipartHttpServletRequestBuilder multipart = multipart(PUT, URI.create(SINGULAR + "/" + draft.getId()));
		for (final MockMultipartFile mockMultipartFile : mockMultipartFiles) multipart.file(mockMultipartFile);
		final ResultActions resultActions = mockMvc.perform(
				multipart
					.contentType(MediaType.MULTIPART_FORM_DATA)
					.params(params)
			)
		;

		// then
		resultActions
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data", is(Integer.valueOf(String.valueOf(draft.getId())))))
			.andDo(
				document(
					"draft_update/updateOne_withValidParamsAndFilesAndId_return200",
					requestParameters(
						parameterWithName("customerName").description("This field shouldn't be empty."),
						parameterWithName("price").description("This field should be greater or equal than 1000."),
						parameterWithName("address").description("This field shouldn't be null."),
						parameterWithName("memo").description("This field shouldn't be null."),
						parameterWithName("password").description("This field shouldn't be empty."),
						parameterWithName("draftStatus").description("This field shouldn't be null."),
						parameterWithName("paymentMethod").description("This field shouldn't be null."),
						parameterWithName("files").optional().description("This parameter is optional. Each file max size is 5MB.")
					),
					responseFields(
						fieldWithPath("status").type(STRING).description("Status value."),
						fieldWithPath("code").type(NUMBER).description("Status code."),
						fieldWithPath("data").type(NUMBER).description("This is the original draft id.")
					)
				)
			)
		;
		final Response.OK<String> ok = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), new TypeReference<>(){});
		final long draftId = Long.parseLong(ok.getData());
		final Path path = Path.of(FolderType.DRAFT.buildPathFrom(storage.getRoot(), draftId));
		assertThat(Objects.requireNonNull(path.toFile().listFiles()).length).isEqualTo(4);
	}

}
