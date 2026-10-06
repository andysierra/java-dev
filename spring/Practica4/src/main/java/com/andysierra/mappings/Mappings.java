package com.andysierra.mappings;

import org.mapstruct.Mapper;
import com.andysierra.model.impl.Usuario;
import com.andysierra.request.UsuarioRequest;

@Mapper(componentModel = "spring")
public interface Mappings {

	public Usuario usuarioRequest2usuario(UsuarioRequest usuarioRequest);
	
}
