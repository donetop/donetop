package com.donetop.main.api;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

	@GetMapping("/test")
	public String test() {
		return new TestDto(10, "jin").toString();
	}

	@Data
	@RequiredArgsConstructor
	static class TestDto {
		private final int age;
		private final String name;
	}

}
