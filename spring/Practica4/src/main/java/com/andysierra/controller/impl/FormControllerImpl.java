package com.andysierra.controller.impl;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.andysierra.controller.interfaces.BaseController;

@Controller
@RequestMapping(
	path = "/FormController"
)
public class FormControllerImpl implements BaseController {

	@RequestMapping(
		path = "/form", 
		method = RequestMethod.GET
	)
	public String form() {
		return "form";
	}
	
	@RequestMapping(
		path = "/show", 
		method = RequestMethod.POST
	)
	public String show() {
		return "showFormResult";
	}
}
