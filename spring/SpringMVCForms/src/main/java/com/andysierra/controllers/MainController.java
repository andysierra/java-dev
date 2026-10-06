package com.andysierra.controllers;

import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.andysierra.request.AlumnoRequest;
import com.andysierra.services.PopulateExampleFormService;

@Controller
@RequestMapping(path = "/API")
public class MainController implements IBasicController {

	
	@RequestMapping(path = "/index")
	public String index() {
		return "index";
	}

	
	@RequestMapping(path = "/exampleForm")
	public String form(Model model) {
		
		model.addAttribute("alumnoModel", new AlumnoRequest());
		model.addAttribute("alumnoValues", new PopulateExampleFormService()); // TODO: temporal mientras aprendo como mapear en JSP los lookupvalues
		
		return "exampleForm";
	}
	
	
	@RequestMapping(path = "/result")
	public String result(@Valid @ModelAttribute("alumnoModel") AlumnoRequest alumnoModel, BindingResult bindingResult) {
		System.out.println(bindingResult.getAllErrors());	// TODO: [conocimiento pendiente] como enviar estos errores a la página result ?
		return bindingResult.hasErrors()? "redirect:/API/exampleForm" : "result";
	}
}
