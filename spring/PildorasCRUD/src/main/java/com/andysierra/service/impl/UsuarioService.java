package com.andysierra.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.andysierra.entity.impl.Usuario;
import com.andysierra.request.impl.UsuarioCreateRequest;
import com.andysierra.response.impl.UsuarioCreateResponse;
import com.andysierra.service.BaseService;
import com.andysierra.util.Mappings;

@Service
public class UsuarioService implements BaseService {
	
	@Autowired
	Mappings mappings;
	
	public UsuarioCreateResponse crearUsuario(UsuarioCreateRequest usuarioCreateRequest) {
		UsuarioCreateResponse resultado = new UsuarioCreateResponse();
		
		Usuario usuario = mappings.usuarioCreateRequest_Usuario(usuarioCreateRequest);

		
		
		return resultado;
	}
	
}
