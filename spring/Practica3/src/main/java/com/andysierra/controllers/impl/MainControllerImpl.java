package com.andysierra.controllers.impl;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.andysierra.controllers.MainController;

@Controller
@RequestMapping(path = "/API")
public class MainControllerImpl implements MainController {
	
	public MainControllerImpl() {
		
	}
	
	@RequestMapping(path = "/index", method = RequestMethod.GET)
	public String index() {
		return "index";
	}
}
