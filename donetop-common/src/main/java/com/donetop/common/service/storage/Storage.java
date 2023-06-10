package com.donetop.common.service.storage;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
@Validated
@Configuration
@ConfigurationProperties(prefix = "application.properties.storage")
public class Storage {

	@NotEmpty
	private String root;

}
