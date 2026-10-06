package com.andysierra.util;

import java.util.Arrays;
import java.util.List;
import com.andysierra.dto.impl.DatoDTO;

public class Parametros {
	
	// TODO: Simulating database query
	public static DatoDTO<List<String>> getPrefijosFuncionalesPorvenir() {
		DatoDTO<List<String>> resultado = new DatoDTO<>();
		
		resultado.setNombre("prefijos funcionales porvenir");
		resultado.setValor(Arrays.asList(new String[]{
				"POR",
				"SIS"
		}));
		resultado.setDescripcion("prefijos funcionales porvenir");
		
		return resultado;
	}
	
}