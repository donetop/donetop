package com.donetop.main.api.common;

import com.donetop.main.properties.ApplicationProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.env.Environment;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.restassured3.RestAssuredRestDocumentation;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.donetop.common.Profile.TEST;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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

	@Autowired
	protected Environment environment;

	protected RequestSpecification spec;

	@BeforeEach
	void beforeEach(final RestDocumentationContextProvider provider) {
		RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder()
			.addFilter(
				RestAssuredRestDocumentation.documentationConfiguration(provider)
					.operationPreprocessors()
					.withRequestDefaults(prettyPrint())
					.withResponseDefaults(prettyPrint())
			)
			.setBaseUri(applicationProperties.getBaseUri())
			.setPort(port);

		if (List.of(environment.getActiveProfiles()).contains(TEST))
			requestSpecBuilder.addFilters(List.of(new RequestLoggingFilter(), new ResponseLoggingFilter()));

		this.spec = requestSpecBuilder.build();
	}

}
