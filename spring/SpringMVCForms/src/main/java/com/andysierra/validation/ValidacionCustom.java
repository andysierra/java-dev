package com.andysierra.validation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.List;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;
import com.andysierra.util.Parametros;

@Documented
@Retention(RUNTIME)
@Target(TYPE)
@Constraint(validatedBy = ValidacionCustom.Validaciones.class)
public @interface ValidacionCustom {

	int tipoValidacion();
	String mensaje();
	String campo();
	String campoDeReferencia() default "";
	
	@Documented
	@Retention(RUNTIME)
	@Target(TYPE)
	@interface DependeDe { ValidacionCustom[] listaValidacionesCustom(); }
	
	public class Validaciones implements ConstraintValidator<ValidacionCustom, Object>{
		
		public static final int ES_IGUAL_A 	= 10;
		public static final int ES_ID_PORVENIR = 20;
		private ValidacionCustom val;
		String campo;
		String campoDeReferencia;
		
		@Override
		public void initialize(ValidacionCustom validacionCustom) {
			
			ConstraintValidator.super.initialize(validacionCustom);
			val = validacionCustom;
			campo = validacionCustom.campo();
			campoDeReferencia = validacionCustom.campoDeReferencia();
		}

		@Override
		public boolean isValid(Object listaValidacionesCustom, ConstraintValidatorContext context) { // TODO: probar los beneficios de context
			
			Object campoVal = new BeanWrapperImpl().getPropertyValue(campo);
			Object campoDeReferenciaVal = new BeanWrapperImpl().getPropertyValue(campoDeReferencia);
			
			switch (val.tipoValidacion()) {
			
				case ES_IGUAL_A: {
					return (campoVal.equals(campoDeReferenciaVal));
				}
				
				case ES_ID_PORVENIR: {
					List<String> prefijos = Parametros.getPrefijosFuncionalesPorvenir().getValor();
					boolean match = false;
					for(String prefijo : prefijos) {
						if(campoVal.equals(prefijo)) {
							match = true;
							break;
						}
					}
					return match;
				}
			}
			return false;
		}
	}
}
