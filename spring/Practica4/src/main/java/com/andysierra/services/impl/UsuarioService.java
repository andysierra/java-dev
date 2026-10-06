package com.andysierra.services.impl;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import com.andysierra.mappings.Mappings;
import com.andysierra.model.impl.Usuario;
import com.andysierra.request.UsuarioRequest;
import com.andysierra.services.interfaces.BaseService;

@Service
public class UsuarioService implements BaseService {

	Mappings mappings = Mappers.getMapper(Mappings.class);
	
	public void saveUsuario(UsuarioRequest usuarioRequest) {
		Usuario usuario = mappings.usuarioRequest2usuario(usuarioRequest);
		System.out.println(usuario);
	}
	
}
