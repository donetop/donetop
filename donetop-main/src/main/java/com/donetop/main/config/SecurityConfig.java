package com.donetop.main.config;

import com.donetop.enums.user.RoleType;
import com.donetop.main.api.authentication.form.FormAPIController;
import com.donetop.main.api.authentication.form.FormAuthenticationFilter;
import com.donetop.main.api.authentication.form.FormAuthenticationFilter.SimpleAuthenticationFailureHandler;
import com.donetop.main.api.authentication.form.FormAuthenticationFilter.SimpleAuthenticationSuccessHandler;
import com.donetop.main.api.draft.DraftAPIController;
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
		FormAPIController.Uri.AUTHENTICATION, FormAPIController.Uri.LOGOUT,
		UserAPIController.Uri.PLURAL,
		DraftAPIController.Uri.SINGULAR, DraftAPIController.Uri.PLURAL
	};

	private final ObjectMapper objectMapper;

	private final FormLoginService formLoginService;

	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		http
			.csrf().disable()
			.authorizeRequests()
				.antMatchers(PUBLIC).permitAll()
				.antMatchers("/**/*.html").hasAuthority(RoleType.ADMIN.name())
				.anyRequest().authenticated()
			.and()
				.addFilterAt(formAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
				.formLogin()
				.loginPage(ViewController.Uri.LOGIN)
			.and()
				.logout()
				.logoutUrl(FormAPIController.Uri.LOGOUT)
				.invalidateHttpSession(true)
		;
	}

	@Bean
	public FormAuthenticationFilter formAuthenticationFilter() throws Exception {
		FormAuthenticationFilter formAuthenticationFilter = new FormAuthenticationFilter(objectMapper);
		formAuthenticationFilter.setAuthenticationSuccessHandler(new SimpleAuthenticationSuccessHandler(objectMapper));
		formAuthenticationFilter.setAuthenticationFailureHandler(new SimpleAuthenticationFailureHandler(objectMapper));
		formAuthenticationFilter.setAuthenticationManager(authenticationManagerBean());
		return formAuthenticationFilter;
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