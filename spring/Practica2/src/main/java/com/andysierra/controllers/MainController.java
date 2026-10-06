package com.andysierra.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(path = "/API")
public class MainController {

	@RequestMapping(method = RequestMethod.GET, value = "/")
	public String index() {
		return "index";
	}
	
}