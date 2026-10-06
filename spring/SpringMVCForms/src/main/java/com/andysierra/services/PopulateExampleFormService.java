package com.andysierra.services;

import java.util.LinkedHashMap;
import java.util.List;

import com.andysierra.dao.FormValuesDao;
import com.andysierra.request.AlumnoRequest;

public class PopulateExampleFormService {
	
	private AlumnoRequest alumnoModel;
	
	
	
	public AlumnoRequest getAlumnoModel() {
		return alumnoModel;
	}



	public void setAlumnoModel(AlumnoRequest alumnoModel) {
		this.alumnoModel = alumnoModel;
	}



	public LinkedHashMap<String, String> optativaValues() {
		return new FormValuesDao().queryAlumnoOptativas();
	}



	public List<String> ciudadEstudioValues() {
		return new FormValuesDao().queryCiudadEstudios();
	}
}
