package co.com.asierra.r2dbc.exceptions;

import org.springframework.dao.DataIntegrityViolationException;

public class SucursalNoEncontradaException extends DataIntegrityViolationException {
    public SucursalNoEncontradaException(String msg) {
        super(msg);
    }
}
