package com.andysierra.request;

import javax.validation.constraints.*;
import org.hibernate.validator.constraints.Range;

public class UsuarioRequest {
	
	private Integer id;

	@NotNull
	@Size(min=3)
	@Pattern(regexp = "^(.+)@(.+)$", message = "El correo esta mal")
	private String correo;

	@NotNull
	@Range(min = 1, max = 120)
	private Integer edad;

	@NotNull
	@Size(min=2)
	private String nombre;

	public UsuarioRequest() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCorreo() {
		return this.correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public Integer getEdad() {
		return this.edad;
	}

	public void setEdad(Integer edad) {
		this.edad = edad;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Override
	public String toString() {
		return "Usuario [id=" + id + ", correo=" + correo + ", edad=" + edad + ", nombre=" + nombre + "]";
	}
}

