package com.andysierra.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/API")
public class MainController {

	@RequestMapping("/")
	public String main() {
		return "Index";
	}
	
}