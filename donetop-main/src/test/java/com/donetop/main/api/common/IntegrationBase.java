package com.donetop.main.api.common;

import com.donetop.main.properties.ApplicationProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.restassured3.RestAssuredRestDocumentation;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class IntegrationBase {

	@RegisterExtension
	protected RestDocumentationExtension restDocumentation = new RestDocumentationExtension("build/generated-snippets");

	@Autowired
	protected ApplicationProperties applicationProperties;

	@Autowired
	protected ObjectMapper objectMapper;

	@LocalServerPort
	protected int port;

	protected MockMvc mockMvc;

	protected RequestSpecification spec;

	@BeforeEach
	void beforeEach(final WebApplicationContext ctx, final RestDocumentationContextProvider provider) {
		this.mockMvc = MockMvcBuilders
			.webAppContextSetup(ctx)
			.apply(
				MockMvcRestDocumentation.documentationConfiguration(provider)
					.operationPreprocessors()
					.withRequestDefaults(prettyPrint())
					.withResponseDefaults(prettyPrint())
			)
			.alwaysDo(print())
			.build();

		this.spec = new RequestSpecBuilder()
			.addFilter(
				RestAssuredRestDocumentation.documentationConfiguration(provider)
					.operationPreprocessors()
					.withRequestDefaults(prettyPrint())
					.withResponseDefaults(prettyPrint())
			)
			.setBaseUri(applicationProperties.getBaseUri())
			.setPort(port)
			.build();
	}

}
