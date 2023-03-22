package com.donetop.oss.config;

import com.donetop.oss.api.form.FormAPIController;
import com.donetop.oss.api.form.filter.LoginFilter;
import com.donetop.oss.api.form.handler.LoginFailureHandler;
import com.donetop.oss.api.form.handler.LoginSuccessHandler;
import com.donetop.oss.api.form.handler.LogoutSuccessHandler;
import com.donetop.oss.view.ViewController;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final String[] PUBLIC = new String[] {
		ViewController.URI.ROOT, ViewController.URI.VIEW,
		FormAPIController.URI.LOGIN, FormAPIController.URI.LOGOUT,
	};

	private static final String[] STATIC_RESOURCES = new String[] {
		"/**/*.js", "/**/*.css", "/**/*.gif",
		"/**/*.png", "/**/*.jpg", "/**/*.jpeg", "/favicon.ico",
		"/**/*.woff", "/**/*.woff2", "/**/*.svg", "/**/*.eot"
	};

	private final ObjectMapper objectMapper;

	private final UserDetailsService userDetailsService;

	private final LoginSuccessHandler loginSuccessHandler;

	private final LoginFailureHandler loginFailureHandler;

	private final LogoutSuccessHandler logoutSuccessHandler;

	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		http
			.csrf().disable()
			.authorizeRequests()
				.antMatchers(STATIC_RESOURCES).permitAll()
				.antMatchers(PUBLIC).permitAll()
				.anyRequest().authenticated()
			.and()
				.addFilterAt(loginFilter(), UsernamePasswordAuthenticationFilter.class)
				.formLogin()
				.loginPage(ViewController.URI.LOGIN)
			.and()
				.logout()
				.logoutUrl(FormAPIController.URI.LOGOUT)
				.logoutSuccessHandler(logoutSuccessHandler)
				.invalidateHttpSession(true)
		;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new PasswordEncoder() {
			@Override
			public String encode(CharSequence rawPassword) {
				return rawPassword.toString();
			}
			@Override
			public boolean matches(CharSequence rawPassword, String encodedPassword) {
				return rawPassword.toString().equals(encodedPassword);
			}
		};
	}

	@Bean
	public LoginFilter loginFilter() throws Exception {
		LoginFilter loginFilter = new LoginFilter(objectMapper);
		loginFilter.setAuthenticationSuccessHandler(loginSuccessHandler);
		loginFilter.setAuthenticationFailureHandler(loginFailureHandler);
		loginFilter.setAuthenticationManager(authenticationManagerBean());
		return loginFilter;
	}

	@Override
	protected void configure(final AuthenticationManagerBuilder auth) {
		auth.authenticationProvider(daoAuthenticationProvider());
	}

	public DaoAuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(userDetailsService);
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		daoAuthenticationProvider.setHideUserNotFoundExceptions(false); // true 로 설정하면 UsernameNotFoundException -> BadCredentialsException 으로 숨겨짐.
		return daoAuthenticationProvider;
	}
}
