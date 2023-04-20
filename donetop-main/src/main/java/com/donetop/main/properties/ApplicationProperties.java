package com.donetop.main.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter @Setter
@Validated
@Configuration
@ConfigurationProperties(prefix = "application.properties")
public class ApplicationProperties {

	@NotEmpty
	private String baseUri;

	@Min(60)
	private int customMaxInactiveInterval;

	@NotNull
	private Storage storage;

	@Getter @Setter
	public static class Storage {

		@NotEmpty
		private String root;

		private String src;

	}

}
