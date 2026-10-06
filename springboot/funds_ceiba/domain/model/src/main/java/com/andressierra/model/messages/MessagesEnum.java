package com.andressierra.model.messages;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum MessagesEnum {

    FUND_SUBSCRIBED(201, "Suscripcion exitosa al fondo", "21"),
    FUND_CANCELLED(200, "Cancelacion exitosa del fondo", "22"),
    TRANSACTIONS_FOUND(200, "Historial de transacciones encontrado", "23"),
    FUNDS_FOUND(200, "Fondos encontrados", "24"),

    BAD_REQUEST(400, "Datos de entrada invalidos", "40"),
    INSUFFICIENT_BALANCE(400, "No tiene saldo disponible para vincularse al fondo", "41"),
    FUND_NOT_FOUND(404, "Fondo no encontrado", "42"),
    CLIENT_NOT_FOUND(404, "Cliente no encontrado", "43"),
    ALREADY_SUBSCRIBED(409, "Ya esta suscrito a este fondo", "44"),
    NOT_SUBSCRIBED(400, "No esta suscrito a este fondo", "45"),
    MINIMUM_AMOUNT_NOT_MET(400, "El monto minimo de vinculacion no se cumple", "46"),

    CONCURRENT_MODIFICATION(409, "Operacion en conflicto, intente nuevamente", "47"),
    PERSISTENCE_ERROR(409, "Error de persistencia", "48"),
    NOTIFICATION_ERROR(500, "Error enviando la notificacion", "51"),
    UNKNOWN_ERROR(500, "Error desconocido, estaremos reparandolo muy pronto", "52");

    private final int code;
    private final String message;
    private final String operationCode;

    public static MessagesEnum findByOpCode(String operationCode) {
        for (MessagesEnum value : values()) {
            if (operationCode.equals(value.operationCode)) {
                return value;
            }
        }
        return null;
    }
}