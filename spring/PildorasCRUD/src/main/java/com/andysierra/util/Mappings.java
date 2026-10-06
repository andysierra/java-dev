package com.andysierra.util;

import org.mapstruct.Mapper;

import com.andysierra.entity.impl.Usuario;
import com.andysierra.request.impl.UsuarioCreateRequest;

@Mapper(componentModel = "spring")
public interface Mappings {

	Usuario usuarioCreateRequest_Usuario(UsuarioCreateRequest request);
	
}
