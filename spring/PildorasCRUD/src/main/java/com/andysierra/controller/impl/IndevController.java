package com.andysierra.controller.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.andysierra.controller.BaseController;
import com.andysierra.request.impl.UsuarioCreateRequest;
import com.andysierra.response.impl.UsuarioCreateResponse;
import com.andysierra.service.impl.UsuarioService;

@RestController
@RequestMapping(path = "/API")
public class IndevController implements BaseController {

	@Autowired
	UsuarioService usuarioService;
	
	@PostMapping(path = "/crear", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody UsuarioCreateResponse crear(@RequestBody UsuarioCreateRequest request) {
		System.out.println("llego al annotated method handler!");
		return usuarioService.crearUsuario(request);
	}
	
	@GetMapping(path = "/greeting", produces = "application/json")
	public void greeting() {
		System.out.println("HELLO PERRA");
	}
}
