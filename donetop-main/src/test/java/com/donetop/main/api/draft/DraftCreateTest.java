package com.donetop.main.api.draft;

import com.donetop.enums.folder.FolderType;
import com.donetop.main.api.common.IntegrationBase;
import com.donetop.main.common.TestFileUtil;
import com.donetop.main.properties.ApplicationProperties.Storage;
import com.donetop.repository.draft.DraftRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.restassured3.RestAssuredRestDocumentation;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import static com.donetop.main.api.common.Response.*;
import static com.donetop.main.api.draft.DraftAPIController.Uri.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
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

	@AfterAll
	void afterAll() throws IOException {
		draftRepository.deleteAll();
		FileSystemUtils.deleteRecursively(Path.of(applicationProperties.getStorage().getRoot()));
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
	void createOne_withValidParamsAndSizeExceedFiles_return400() {
		// given
		final Storage storage = applicationProperties.getStorage();
		final List<File> files = TestFileUtil.readFiles(Path.of(storage.getSrc()));
		final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("customerName", "jin");
		params.add("address", "my address");
		params.add("price", "1000");
		params.add("memo", "simple test");
		params.add("password", "my password");
		RequestSpecification given = RestAssured.given(this.spec);
		for (final File file : files) given.multiPart("files", file);
		given.params(params);
		given.filter(
			RestAssuredRestDocumentation.document(
				"draft_create/createOne_withValidParamsAndSizeExceedFiles_return400"
			)
		);

		// when
		final Response response = given.when().post(SINGULAR);

		// then
		response.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.body("reason", containsString("SizeLimitExceededException"));
	}

	@Test
	void createOne_withValidParamsAndFiles_return200() throws Exception {
		// given
		final Storage storage = applicationProperties.getStorage();
		final List<MockMultipartFile> mockMultipartFiles = TestFileUtil.readMultipartFiles(Path.of(storage.getSrc()));
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
					requestParameters(
						parameterWithName("customerName").description("This parameter shouldn't be empty."),
						parameterWithName("price").description("This parameter should be greater or equal than 1000."),
						parameterWithName("address").description("This parameter shouldn't be null."),
						parameterWithName("memo").description("This parameter shouldn't be null."),
						parameterWithName("password").description("This parameter shouldn't be empty."),
						parameterWithName("files").optional().description("This parameter is optional. Each file max size is 5MB.")
					),
					responseFields(
						fieldWithPath("status").type(STRING).description("Status value."),
						fieldWithPath("code").type(NUMBER).description("Status code."),
						fieldWithPath("data").type(NUMBER).description("This is auto generated draft id.")
					)
				)
			)
		;
		final OK<String> ok = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), new TypeReference<>(){});
		final long draftId = Long.parseLong(ok.getData());
		final Path path = Path.of(FolderType.DRAFT.buildPathFrom(storage.getRoot(), draftId));
		assertThat(Objects.requireNonNull(path.toFile().listFiles()).length).isEqualTo(4);
	}

}
