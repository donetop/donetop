package com.donetop.oss.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter @Setter
@Validated
@Configuration
@ConfigurationProperties(prefix = "application.properties")
public class ApplicationProperties {

	@NotEmpty
	private String baseUri;

	@NotNull
	private Storage storage;

	@NotEmpty
	private String cookieName;

	@Getter @Setter
	public static class Storage {

		@NotEmpty
		private String root;

		private String src;

	}

}
