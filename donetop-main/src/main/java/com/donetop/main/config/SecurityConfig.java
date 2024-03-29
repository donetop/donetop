package com.donetop.main.config;

import com.donetop.common.encoder.NoOpPasswordEncoder;
import com.donetop.enums.user.RoleType;
import com.donetop.main.api.category.CategoryAPIController;
import com.donetop.main.api.draft.DraftCommentAPIController;
import com.donetop.main.api.draft.DraftAPIController;
import com.donetop.main.api.enums.EnumAPIController;
import com.donetop.main.api.file.FileAPIController;
import com.donetop.main.api.form.FormAPIController;
import com.donetop.main.api.form.filter.ContentCachingRequestFilter;
import com.donetop.main.api.form.filter.LoginFilter;
import com.donetop.main.api.form.handler.LoginFailureHandler;
import com.donetop.main.api.form.handler.LoginSuccessHandler;
import com.donetop.main.api.form.handler.LogoutSuccessHandler;
import com.donetop.common.form.InvalidCookieClearingStrategy;
import com.donetop.main.api.notice.NoticeAPIController;
import com.donetop.main.api.post.CustomerPostAPIController;
import com.donetop.main.api.post.CustomerPostCommentAPIController;
import com.donetop.main.api.user.UserAPIController;
import com.donetop.main.properties.ApplicationProperties;
import com.donetop.main.view.ViewController;
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
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.firewall.HttpStatusRequestRejectedHandler;
import org.springframework.security.web.firewall.RequestRejectedHandler;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import static com.donetop.common.Profile.LOCAL;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private static final String[] PUBLIC = new String[] {
		ViewController.URI.ROOT, ViewController.URI.VIEW,
		FormAPIController.URI.LOGIN, FormAPIController.URI.LOGOUT,
		UserAPIController.URI.SINGULAR,
		DraftAPIController.URI.SINGULAR + "/**", DraftAPIController.URI.PLURAL + "/**",
		DraftCommentAPIController.URI.SINGULAR + "/**",
		FileAPIController.URI.SINGULAR + "/**",
		EnumAPIController.URI.ROOT + "/**",
		CategoryAPIController.URI.PLURAL + "/**", CategoryAPIController.URI.SINGULAR + "/**",
		com.donetop.main.api.nhn.URI.NHN_API + "/**",
		CustomerPostAPIController.URI.SINGULAR + "/**", CustomerPostAPIController.URI.PLURAL + "/**",
		CustomerPostCommentAPIController.URI.SINGULAR + "/**",
		NoticeAPIController.URI.PLURAL + "/**"
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

	private final AuthenticationEntryPoint authenticationEntryPoint;

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
				.addFilterBefore(contentCachingRequestFilter(), LoginFilter.class)
				.formLogin()
				.loginPage(ViewController.URI.LOGIN)
			.and()
				.logout()
				.logoutUrl(FormAPIController.URI.LOGOUT)
				.logoutSuccessHandler(logoutSuccessHandler)
				.invalidateHttpSession(true)
				.deleteCookies(applicationProperties.getCookieName())
			.and()
				.sessionManagement()
				.invalidSessionStrategy(new InvalidCookieClearingStrategy(applicationProperties.getCookieName()))
			.and()
				.exceptionHandling()
				.authenticationEntryPoint(authenticationEntryPoint)
		;
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

	@Bean
	public ContentCachingRequestFilter contentCachingRequestFilter() {
		return new ContentCachingRequestFilter();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}

	@Bean
	public RequestRejectedHandler requestRejectedHandler() {
		return new HttpStatusRequestRejectedHandler();
	}

	@Override
	protected void configure(final AuthenticationManagerBuilder auth) {
		auth.authenticationProvider(daoAuthenticationProvider());
	}

	public DaoAuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(userDetailsService);
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		daoAuthenticationProvider.setHideUserNotFoundExceptions(true); // true 로 설정하면 UsernameNotFoundException -> BadCredentialsException 으로 숨겨짐.
		return daoAuthenticationProvider;
	}
}
