package com.donetop.main.view;

import com.donetop.main.service.authentication.FormLoginService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static com.donetop.main.view.ViewController.Uri.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ViewController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class ViewControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private FormLoginService formLoginService;

	@Test
	void view() throws Exception {
		mockMvc.perform(get(ROOT))
			.andDo(print())
			.andExpect(status().isOk());
		mockMvc.perform(get(HOME))
			.andDo(print())
			.andExpect(status().isOk());
		mockMvc.perform(get(LOGIN))
			.andDo(print())
			.andExpect(status().isOk());
	}
}