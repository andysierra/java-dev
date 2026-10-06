package com.andysierra.dto.impl;

import java.io.Serializable;
import com.andysierra.dto.IBasicDTO;

public class DatoDTO <T> implements IBasicDTO, Serializable{
	private static final long serialVersionUID = 1L;
	
	private String nombre;
	private T valor;
	private String descripcion;
	private String estado;
	
	public DatoDTO(){}
	
	public DatoDTO(String nombre, T valor, String estado) {
		super();
		this.nombre = nombre;
		this.valor = valor;
		this.estado = estado;
	}

	
	
	
	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public T getValor() {
		return valor;
	}

	public void setValor(T valor) {
		this.valor = valor;
	}

	@Override
	public String getDescripcion() {
		return this.descripcion;
	}

	@Override
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
		
	}

	@Override
	public String getEstado() {
		return this.estado;
	}

	@Override
	public void setEstado(String estado) {
		this.estado = estado;
	}

	@Override
	public String toString() {
		return "ParametroDTO [nombre=" + nombre + ", valor=" + valor + ", descripcion=" + descripcion + ", estado="
				+ estado + "]";
	}
}