package com.andysierra.models.impl;

import com.andysierra.models.IModel;

public class Cliente implements IModel {

	private Long id;
	private String nombre;
	private String apellido;
	private String email;
	
	public Cliente(){}

	public Cliente(Long id, String nombre, String apellido, String email) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.apellido = apellido;
		this.email = email;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "Cliente [id=" + id + ", nombre=" + nombre + ", apellido=" + apellido + ", email=" + email + "]";
	}

	@Override
	public String toIndentedString() {
		return "Cliente [\nid=" + id + ", \nnombre=" + nombre + ", \napellido=" + apellido + ", \nemail=" + email + "]";
	}
}
