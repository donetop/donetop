package com.donetop.main.apis;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeAPIController {

	@GetMapping(value = {"/", "/home"})
	public String home() {
		return "index";
	}

}
