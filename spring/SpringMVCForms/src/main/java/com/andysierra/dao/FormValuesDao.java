package com.andysierra.dao;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class FormValuesDao {

	// TODO: Simulating database query
	public LinkedHashMap<String, String> queryAlumnoOptativas() {
		LinkedHashMap<String, String> result = new LinkedHashMap<>();

		result.put("ESP", "Español");
		result.put("INF", "Informatica");
		result.put("BIO", "Biologia");
		result.put("COM", "Comercio");

		return result;
	}

	// TODO: Simulating database query
	public List<String> queryCiudadEstudios() {
		List<String> result = new ArrayList<String>();
		
		result.add("Cali");
		result.add("Bogotá");
		result.add("Medellín");
		result.add("Cartagena");
		
		return result;
	}
}
