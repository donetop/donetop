package com.donetop.main.api.draft;

import com.donetop.main.api.common.IntegrationBase;
import com.donetop.main.common.TestFileUtil;
import com.donetop.repository.draft.DraftRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.net.URI;
import java.nio.file.Path;
import java.util.List;

import static com.donetop.main.api.draft.DraftAPIController.PATH.*;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DraftCreateTest extends IntegrationBase {

	@Autowired
	private DraftRepository draftRepository;

	@Value("${storage.src}")
	private String src;

	@AfterAll
	void afterAll() {
		draftRepository.deleteAll();
	}

	@Test
	void createOne_withoutParams_return400() throws Exception {
		// given
		final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

		// when
		final ResultActions resultActions = mockMvc.perform(
				multipart(POST, URI.create(SINGULAR))
					.contentType(MediaType.MULTIPART_FORM_DATA)
					.params(params)
			)
		;

		// then
		resultActions
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.reason", hasSize(5)))
			.andDo(
				document("draft_create/createOne_withoutParams_return400")
			)
		;
	}

	@Test
	void createOne_withInvalidParams_return400() throws Exception {
		// given
		final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("customerName", "");
		params.add("address", null);
		params.add("price", "0");
		params.add("memo", null);
		params.add("password", "");

		// when
		final ResultActions resultActions = mockMvc.perform(
				multipart(POST, URI.create(SINGULAR))
					.contentType(MediaType.MULTIPART_FORM_DATA)
					.params(params)
			)
		;

		// then
		resultActions
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.reason", hasSize(5)))
			.andDo(
				document("draft_create/createOne_withInvalidParams_return400")
			)
		;
	}

	@Test
	void createOne_withValidParamsAndWithoutFiles_return200() throws Exception {
		// given
		final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("customerName", "jin");
		params.add("address", "my address");
		params.add("price", "1000");
		params.add("memo", "simple test");
		params.add("password", "my password");

		// when
		final ResultActions resultActions = mockMvc.perform(
				multipart(POST, URI.create(SINGULAR))
					.contentType(MediaType.MULTIPART_FORM_DATA)
					.params(params)
			)
		;

		// then
		resultActions
			.andExpect(status().isOk())
			.andDo(
				document("draft_create/createOne_withValidParamsAndWithoutFiles_return200")
			)
		;
	}

	@Test
	void createOne_withValidParamsAndFiles_return200() throws Exception {
		// given
		final List<MockMultipartFile> mockMultipartFiles = TestFileUtil.readMultipartFiles(Path.of(src));
		final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("customerName", "jin");
		params.add("address", "my address");
		params.add("price", "1000");
		params.add("memo", "simple test");
		params.add("password", "my password");

		// when
		MockMultipartHttpServletRequestBuilder multipart = multipart(POST, URI.create(SINGULAR));
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
			.andDo(
				document(
					"draft_create/createOne_withValidParamsAndFiles_return200",
					preprocessRequest(),
					preprocessResponse(),
					requestParameters(
						parameterWithName("customerName").description("This parameter shouldn't be empty."),
						parameterWithName("price").description("This parameter should be greater or equal than 1000."),
						parameterWithName("address").description("This parameter shouldn't be null."),
						parameterWithName("memo").description("This parameter shouldn't be null."),
						parameterWithName("password").description("This parameter shouldn't be empty."),
						parameterWithName("files").optional().description("This parameter is optional.")
					),
					responseFields(
						fieldWithPath("status").type(STRING).description("Status value."),
						fieldWithPath("code").type(NUMBER).description("Status code."),
						fieldWithPath("data").type(NUMBER).description("This is auto generated draft id.")
					)
				)
			)
		;
	}

}
