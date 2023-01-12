package com.donetop.main.config;

import com.donetop.enums.user.RoleType;
import com.donetop.main.api.draft.DraftAPIController;
import com.donetop.main.api.form.FormAPIController;
import com.donetop.main.api.form.LoginFilter;
import com.donetop.main.api.form.handler.LoginFailureHandler;
import com.donetop.main.api.form.handler.LoginSuccessHandler;
import com.donetop.main.api.form.handler.LogoutSuccessHandler;
import com.donetop.main.api.user.UserAPIController;
import com.donetop.main.service.authentication.FormLoginService;
import com.donetop.main.view.ViewController;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private static final String[] PUBLIC = new String[] {
		ViewController.Uri.ROOT, ViewController.Uri.HOME, ViewController.Uri.LOGIN,
		FormAPIController.Uri.LOGIN, FormAPIController.Uri.LOGOUT,
		UserAPIController.Uri.PLURAL,
		DraftAPIController.Uri.SINGULAR + "/**", DraftAPIController.Uri.PLURAL + "/**"
	};

	private static final String[] STATIC_RESOURCES = new String[] {
		"/**/*.js", "/**/*.css",
		"/**/*.png", "/**/*.jpg", "/**/*.jpeg", "favicon.ico",
		"/**/*.woff", "/**/*.woff2", "/**/*.svg", "/**/*.eot"
	};

	private final ObjectMapper objectMapper;

	private final FormLoginService formLoginService;

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
				.antMatchers("/**/*.html").hasAuthority(RoleType.ADMIN.name())
				.anyRequest().authenticated()
			.and()
				.addFilterAt(loginFilter(), UsernamePasswordAuthenticationFilter.class)
				.formLogin()
				.loginPage(ViewController.Uri.LOGIN)
			.and()
				.logout()
				.logoutUrl(FormAPIController.Uri.LOGOUT)
				.logoutSuccessHandler(logoutSuccessHandler)
				.invalidateHttpSession(true)
		;
	}

	@Bean
	public LoginFilter loginFilter() throws Exception {
		LoginFilter loginFilter = new LoginFilter(objectMapper);
		loginFilter.setAuthenticationSuccessHandler(loginSuccessHandler);
		loginFilter.setAuthenticationFailureHandler(loginFailureHandler);
		loginFilter.setAuthenticationManager(authenticationManagerBean());
		return loginFilter;
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

	@Override
	protected void configure(final AuthenticationManagerBuilder auth) {
		auth.authenticationProvider(daoAuthenticationProvider());
	}

	public DaoAuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(formLoginService);
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		daoAuthenticationProvider.setHideUserNotFoundExceptions(false); // true 로 설정하면 UsernameNotFoundException -> BadCredentialsException 으로 숨겨짐.
		return daoAuthenticationProvider;
	}
}
