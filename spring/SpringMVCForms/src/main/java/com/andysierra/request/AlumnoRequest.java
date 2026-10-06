package com.andysierra.request;

import javax.validation.constraints.Size;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;

import com.andysierra.validation.ValidacionCustom;
import com.andysierra.validation.ValidacionCustom.Validaciones;

@ValidacionCustom.DependeDe(listaValidacionesCustom = {
	@ValidacionCustom(
		tipoValidacion = Validaciones.ES_ID_PORVENIR,
		campo = "porvenirId",
		mensaje = "No es un id que perteneza a Porvenir!"
	),
	@ValidacionCustom(
		tipoValidacion = Validaciones.ES_IGUAL_A,
		campo = "password",
		campoDeReferencia = "passwordConfirm",
		mensaje = "Las contraseñas no son iguales!"
    )
})
@Validated
public class AlumnoRequest implements IBasicRequest {

	@NonNull
	@Size(min = 2, max = 99, message = "Tu nombre debe estar entre 2 y 99 letras")
	private String nombre;

	@NonNull
	@Size(min = 2, max = 99, message = "Tu apellido debe estar entre 2 y 99 letras")
	private String apellido;

	@NonNull
	@Size(min = 2, max = 99, message = "El nombre de la optativa debe estar entre 2 y 99 letras")
	private String optativa;

	@NonNull
	@Size(min = 2, max = 99, message = "El nombre de la ciudad debe estar entre 2 y 99 letras")
	private String ciudadEstudios;

	// Custom Validated:
	@NonNull
	@Size(min = 2, max = 99, message = "El porvenirId debe estar entre 2 y 99 letras")
	private String porvenirId;
	
	@NonNull
	@Size(min = 2, max = 99, message = "La clave debe estar entre 2 y 99 letras")
	private String password;
	
	@NonNull
	@Size(min = 2, max = 99, message = "La confirmacion debe estar entre 2 y 99 letras")
	private String passwordConfirm;
	
	
	
	
	public AlumnoRequest() {
		super();
	}




	public AlumnoRequest(String nombre, String apellido, String optativa, String ciudadEstudios) {
		super();
		this.nombre = nombre;
		this.apellido = apellido;
		this.optativa = optativa;
		this.ciudadEstudios = ciudadEstudios;
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




	public String getOptativa() {
		return optativa;
	}




	public void setOptativa(String optativa) {
		this.optativa = optativa;
	}




	public String getCiudadEstudios() {
		return ciudadEstudios;
	}




	public void setCiudadEstudios(String ciudadEstudios) {
		this.ciudadEstudios = ciudadEstudios;
	}




	public String getPorvenirId() {
		return porvenirId;
	}




	public void setPorvenirId(String porvenirId) {
		this.porvenirId = porvenirId;
	}




	public String getPassword() {
		return password;
	}




	public void setPassword(String password) {
		this.password = password;
	}




	public String getPasswordConfirm() {
		return passwordConfirm;
	}




	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}




	@Override
	public String toString() {
		return "AlumnoModel [nombre=" + nombre + ", apellido=" + apellido + ", optativa=" + optativa
				+ ", ciudadEstudios=" + ciudadEstudios + ", porvenirId=" + porvenirId + ", password=" + password
				+ ", passwordConfirm=" + passwordConfirm + "]";
	}

}
