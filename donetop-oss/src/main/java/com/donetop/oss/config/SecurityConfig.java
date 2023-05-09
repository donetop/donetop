package com.donetop.oss.config;

import com.donetop.common.encoder.NoOpPasswordEncoder;
import com.donetop.common.form.InvalidCookieClearingStrategy;
import com.donetop.enums.user.RoleType;
import com.donetop.oss.api.category.OSSCategoryAPIController;
import com.donetop.oss.api.file.OSSFileAPIController;
import com.donetop.oss.api.form.OSSFormAPIController;
import com.donetop.oss.api.form.filter.LoginFilter;
import com.donetop.oss.api.form.handler.LoginFailureHandler;
import com.donetop.oss.api.form.handler.LoginSuccessHandler;
import com.donetop.oss.api.form.handler.LogoutSuccessHandler;
import com.donetop.oss.api.user.OSSUserAPIController;
import com.donetop.oss.properties.ApplicationProperties;
import com.donetop.oss.view.OSSViewController;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import static com.donetop.common.Profile.*;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final String[] PUBLIC = new String[] {
		OSSViewController.URI.ROOT, OSSViewController.URI.VIEW,
		OSSFormAPIController.URI.LOGIN, OSSFormAPIController.URI.LOGOUT,
		OSSUserAPIController.URI.SINGULAR,
		OSSCategoryAPIController.URI.SINGULAR + "/**", OSSCategoryAPIController.URI.PLURAL + "/**",
		OSSFileAPIController.URI.SINGULAR + "/**"
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

	private final ApplicationProperties applicationProperties;

	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		http
			.csrf().disable()
			.authorizeRequests()
				.antMatchers(STATIC_RESOURCES).permitAll()
				.antMatchers(PUBLIC).permitAll()
				.antMatchers("/api/**").hasAuthority(RoleType.ADMIN.name())
				.anyRequest().authenticated()
			.and()
				.addFilterAt(loginFilter(), UsernamePasswordAuthenticationFilter.class)
				.formLogin()
				.loginPage(OSSViewController.URI.LOGIN)
			.and()
				.logout()
				.logoutUrl(OSSFormAPIController.URI.LOGOUT)
				.logoutSuccessHandler(logoutSuccessHandler)
				.invalidateHttpSession(true)
				.deleteCookies(applicationProperties.getCookieName())
			.and()
				.sessionManagement()
				.invalidSessionStrategy(new InvalidCookieClearingStrategy(applicationProperties.getCookieName()))
		;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}

	@Bean
	@Profile(value = LOCAL)
	public ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() {
		return new ServletListenerRegistrationBean<>(new HttpSessionEventPublisher());
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
