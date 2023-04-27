package com.donetop.oss.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import static com.donetop.oss.view.OSSViewController.URI.*;

@Controller
public class OSSViewController {

	public static class URI {
		public static final String ROOT = "/";
		public static final String VIEW = "/view/**";
		public static final String LOGIN = "/view/login";
	}

	@GetMapping(value = {ROOT, VIEW, LOGIN})
	public String view() {
		return "index";
	}

}
