package com.andysierra.mappings.test;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import com.andysierra.mappings.Mappings;
import com.andysierra.model.impl.Usuario;
import com.andysierra.request.UsuarioRequest;

//@SpringBootTest
//@RunWith(SpringRunner.class)
public class MappingsTest {

	// prepare mapper
	//@Autowired
	//private Mappings mappings;
	private Mappings mappings = Mappers.getMapper(Mappings.class);
	
	@Test
	public void debeDarmeUnUsuarioDesdeUsuarioRequest() {

		// prepare request
		UsuarioRequest request = new UsuarioRequest();
		request.setId(1993);
		request.setNombre("testable");
		request.setEdad(19);
		request.setCorreo("testable");
		
		Usuario usuario = mappings.usuarioRequest2usuario(request);
		System.out.println(usuario);
		
		assertEquals(usuario.getId(), request.getId());
	}
}
