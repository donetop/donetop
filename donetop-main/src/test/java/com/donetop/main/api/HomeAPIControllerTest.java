package com.donetop.main.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HomeAPIController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class HomeAPIControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void root() throws Exception {
		mockMvc.perform(get("/"))
			.andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	void home() throws Exception {
		mockMvc.perform(get("/home"))
			.andDo(print())
			.andExpect(status().isOk());
	}

}