package com.andysierra.controller.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.andysierra.controller.interfaces.BaseController;
import com.andysierra.model.impl.Usuario;
import com.andysierra.request.UsuarioRequest;
import com.andysierra.services.impl.UsuarioService;

@Controller
@RequestMapping(
	path = "/API"
)
public class MainControllerImpl implements BaseController {
	
	@Autowired
	UsuarioService usuarioService;

	@RequestMapping(
		path = "/", 
		method = RequestMethod.GET
	)
	public String index() {
		return "index";
	}

	@RequestMapping(
		path = "/testForm", 
		method = RequestMethod.POST
	)
	public String testForm(/*@RequestBody UsuarioRequest body*/) {
		//System.out.println(body);
		
		// validations simple not working
		UsuarioRequest usuarioRequest = new UsuarioRequest();
		usuarioRequest.setId(null);
		usuarioRequest.setNombre("no valida ojo");
		usuarioRequest.setEdad(1993);
		usuarioRequest.setCorreo(null);

		usuarioService.saveUsuario(usuarioRequest);
		
		return "";
	}
}
