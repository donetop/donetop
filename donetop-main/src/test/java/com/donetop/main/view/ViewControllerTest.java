package com.donetop.main.view;

import com.donetop.main.api.form.handler.LoginFailureHandler;
import com.donetop.main.api.form.handler.LoginSuccessHandler;
import com.donetop.main.api.form.handler.LogoutSuccessHandler;
import com.donetop.main.properties.ApplicationProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static com.donetop.common.Profile.TEST;
import static com.donetop.main.view.ViewController.URI.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ViewController.class)
@ActiveProfiles(TEST)
@AutoConfigureMockMvc
class ViewControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@TestConfiguration
	public static class TestConfig {
		@MockBean
		private UserDetailsService userDetailsService;
		@MockBean
		private LoginSuccessHandler loginSuccessHandler;
		@MockBean
		private LoginFailureHandler loginFailureHandler;
		@MockBean
		private LogoutSuccessHandler logoutSuccessHandler;
		@MockBean
		private ApplicationProperties applicationProperties;
	}

	@Test
	void view() throws Exception {
		mockMvc.perform(get(ROOT))
			.andDo(print())
			.andExpect(status().isOk());
		mockMvc.perform(get(LOGIN))
			.andDo(print())
			.andExpect(status().isOk());
	}
}