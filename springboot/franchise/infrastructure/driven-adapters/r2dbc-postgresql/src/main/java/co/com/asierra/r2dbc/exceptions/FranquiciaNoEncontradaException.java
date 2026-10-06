package co.com.asierra.r2dbc.exceptions;

import org.springframework.dao.DataIntegrityViolationException;

public class FranquiciaNoEncontradaException extends DataIntegrityViolationException {
    public FranquiciaNoEncontradaException(String msg) {
        super(msg);
    }
}
