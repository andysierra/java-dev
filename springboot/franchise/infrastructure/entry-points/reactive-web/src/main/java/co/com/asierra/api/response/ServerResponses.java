package co.com.asierra.api.response;

import co.com.asierra.api.request.CrearProductoRequest;
import co.com.asierra.api.request.CrearSucursalRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.ZonedDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
public class ServerResponses {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public static <T> Mono<ServerResponse> onError(Throwable throwable, Mono<T> flow) {
        log.error(">>> Error", throwable);
        String errName = throwable.getClass().getSimpleName();

        if(flow == null) {
            if("NumberFormatException".equals(errName)) {
                return buildBadRequestResponse(null, throwable.getMessage(), null);
            }
        }

        return flow.flatMap(body -> {
            if ("FranquiciaNoEncontradaException".equals(errName) && body instanceof CrearSucursalRequest sucursalRequest)
                return buildBadRequestResponse(sucursalRequest, throwable.getMessage(), null);
            else if ("SucursalNoEncontradaException".equals(errName) && body instanceof CrearProductoRequest productoRequest)
                return buildBadRequestResponse(productoRequest, throwable.getMessage(), null);
            else if ("ProductoNoEncontradoException".equals(errName) && body instanceof CrearProductoRequest productoRequest)
                return buildBadRequestResponse(productoRequest, throwable.getMessage(), "El producto no fue encontrado");
            else if ("ElementoNoEncontradoException".equals(errName))
                return buildBadRequestResponse(null, throwable.getMessage(), ">>>>>>>>>> aaaaaaaaaaaaaa");
            else {
                return buildBadRequestResponse(null, throwable.getMessage(), null);
            }
        });
    }



    private static <T> Mono<ServerResponse> buildBadRequestResponse(T request, String errorMessage, String auxMessage) {

        String message = "";

        if(auxMessage != null && !auxMessage.isEmpty()) {
            message = auxMessage;
        }
        else if(request == null) {
            if("NumberFormatException".equals(errorMessage)) {
                message = "El id del producto no es un número válido";
            }
        }
        else if(request instanceof CrearSucursalRequest sucursalRequest) {
            message = String.format("La franquicia con el id %s no existe! %s",
                    sucursalRequest.getFranquiciaId(), sucursalRequest);
        }
        else if(request instanceof CrearProductoRequest productoRequest) {
            message = String.format("La sucursal con el id %s no existe! %s",
                    productoRequest.getSucursalId(), productoRequest);
        }
        else {
            message = "Error desconocido";
        }

        MessageResponse response = MessageResponse.builder()
                .error(errorMessage)
                .message(message)
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(ZonedDateTime.now())
                .build();

        return ServerResponse.badRequest()
                .contentType(MediaType.APPLICATION_NDJSON)
                .bodyValue(toJson(response));
    }



    private static String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException("Error al convertir a JSON", e);
        }
    }
}
