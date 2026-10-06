package com.andysierra.entity.impl;

import java.util.Objects;
import javax.persistence.*;
import com.andysierra.entity.BaseEntity;

@Entity
@Table(name = "usuario")
public class Usuario implements BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long 	id;
	
	@Column(name = "nombre")
	private String 	nombre;
	
	@Column(name = "edad")
	private int 	edad;
	
	@Column(name = "correo")
	private String 	correo;

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

	public int getEdad() {
		return edad;
	}

	public void setEdad(int edad) {
		this.edad = edad;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	@Override
	public int hashCode() {
		return Objects.hash(correo, edad, id, nombre);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		return Objects.equals(correo, other.correo) && edad == other.edad && Objects.equals(id, other.id)
				&& Objects.equals(nombre, other.nombre);
	}

	@Override
	public String toString() {
		return "Usuario [id=" + id + ", nombre=" + nombre + ", edad=" + edad + ", correo=" + correo + "]";
	}
}
