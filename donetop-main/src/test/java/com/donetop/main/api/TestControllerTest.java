package com.donetop.main.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.donetop.main.BaseTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TestControllerTest extends BaseTest {

	@Test
	@DisplayName("/test 요청")
	void test() throws Exception {
		mockMvc.perform( get("/test") )
			.andDo( print() )
			.andExpect( status().isOk() );
	}

}