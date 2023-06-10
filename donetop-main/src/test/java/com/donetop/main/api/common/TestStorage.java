package com.donetop.main.api.common;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

@Validated
@Configuration
@ConfigurationProperties(prefix = "application.properties.storage")
public class TestStorage {

	@NotEmpty
	private String root;

	@NotEmpty
	private String src;

	public String getRoot() {
		return root;
	}

	public void setRoot(final String root) {
		this.root = root;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(final String src) {
		this.src = src;
	}
}
