package com.donetop.collect.config

import com.donetop.collect.api.draft.DraftCollectAPIController
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