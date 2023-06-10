package com.donetop.batch.config

import com.donetop.batch.api.draft.DraftCollectAPIController
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@EnableWebSecurity
class SecurityConfig: WebSecurityConfigurerAdapter() {

	private val public: Array<String> = arrayOf(
		DraftCollectAPIController.URI.COLLECT
	)

	override
	fun configure(http: HttpSecurity) {
		http
			.csrf().disable()
			.authorizeRequests()
				.antMatchers(*public).permitAll()
				.anyRequest().authenticated()
	}
}