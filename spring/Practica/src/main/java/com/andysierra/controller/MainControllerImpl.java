package com.andysierra.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

// TODO Probar si funciona igual implementar una 
// interface @Controller, o debe ir aquí en el impl
@Controller
@RequestMapping("/API")
public class MainControllerImpl implements MainController{

	/**
	 * @apiNote Only acepts GET requests
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index() {
		return "index";
	}
	
}
