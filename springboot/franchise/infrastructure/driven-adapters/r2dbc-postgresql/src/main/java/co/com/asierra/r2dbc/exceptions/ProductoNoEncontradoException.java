package co.com.asierra.r2dbc.exceptions;

import org.springframework.dao.DataIntegrityViolationException;

public class ProductoNoEncontradoException extends DataIntegrityViolationException {
    public ProductoNoEncontradoException(String msg) {
        super(msg);
    }
}
