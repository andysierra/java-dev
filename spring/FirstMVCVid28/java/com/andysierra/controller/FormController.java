package com.andysierra.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/FormAPI")
public class FormController {

	/**
	 * Get JSP with a form
	 * @author Andysierra
	 * @return String to viewResolver
	 */
	@RequestMapping("/form")
	public String getForm() {
		return "form";
	}
	
	
	
	
	/**
	 * Get JSP with a result
	 * @author Andysierra
	 * @return String to viewResolver
	 */
	@RequestMapping("/result")
	public String getResult() {
		return "result";
	}
	
	
	
	
	/**
	 * Get JSP with a result and static files, 
	 * but now, using generic model and RequestParam
	 * 
	 * (Ver como se hace en cajamaynas)
	 * 
	 * @author Andysierra
	 * @return String to viewResolver
	 */
	@RequestMapping("/result2")
	public String getResult2(@RequestParam("age") int age, Model genericModel) {
		genericModel.addAttribute("age", age);
		return "result2";
	}
}