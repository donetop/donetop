package com.donetop.main.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import static com.donetop.main.view.ViewController.Uri.*;

@Controller
public class ViewController {

	public static class Uri {
		public static final String ROOT = "/";
		public static final String HOME = "/home";
		public static final String LOGIN = "/login";
		public static final String DRAFT = "/draft/**";
	}

	@GetMapping(value = {ROOT, HOME, LOGIN, DRAFT})
	public String view() {
		return "index";
	}

}
