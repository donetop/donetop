package com.donetop.main.apis;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.donetop.main.BaseTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class HomeAPIControllerTest extends BaseTest {

	@Test
	@DisplayName("home 화면 정상 응답 테스트")
	void home() throws Exception {
		mockMvc.perform(get("/home"))
			.andDo(print())
			.andExpect(status().isOk());
	}

}