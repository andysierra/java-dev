package co.com.asierra.r2dbc.exceptions;

import org.springframework.dao.DataIntegrityViolationException;

public class ElementoNoEncontradoException extends DataIntegrityViolationException {
    public <I> ElementoNoEncontradoException(String msg, I id) {
        super("Elemento con id {"+id.toString()+"} no encontrado"+msg);
    }
}
