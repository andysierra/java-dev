package co.com.asierra.api;

import co.com.asierra.api.request.CrearFranquiciaRequest;
import co.com.asierra.api.request.CrearProductoRequest;
import co.com.asierra.api.request.CrearSucursalRequest;
import co.com.asierra.api.request.ModificarProductoRequest;
import co.com.asierra.api.request.ModificarStockRequest;
import co.com.asierra.api.response.ServerResponses;
import co.com.asierra.usecase.franchise.FranchiseUseCase;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class Handler {
    private final FranchiseUseCase franchiseUseCase;
    private final Mappings mappings;

    // pendiente validaciones

    public Mono<ServerResponse> crearFranquicia(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(CrearFranquiciaRequest.class)
                .map(mappings::toFranquicia)
                .flatMap(franchiseUseCase::crearFranquicia)
                .flatMap(response ->
                        ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(response));
    }

    public Mono<ServerResponse> crearSucursal(ServerRequest serverRequest) {
        Mono<CrearSucursalRequest> flow = serverRequest.bodyToMono(CrearSucursalRequest.class).cache();

        return flow
                .map(mappings::toSucursal)
                .flatMap(franchiseUseCase::crearSucursal)
                .flatMap(response -> ServerResponse.ok().bodyValue(response))
                .onErrorResume(error -> ServerResponses.onError(error, flow));
    }

    public Mono<ServerResponse> crearProducto(ServerRequest serverRequest) {
        Mono<CrearProductoRequest> flow = serverRequest.bodyToMono(CrearProductoRequest.class).cache();

        return flow
                .flatMap(req -> franchiseUseCase.buscarSucursalPorId(req.getSucursalId())
                        .flatMap(sucursal -> franchiseUseCase.crearProducto(mappings.toProducto(req), sucursal)))
                .flatMap(response -> ServerResponse.ok().bodyValue(response))
                .onErrorResume(error -> ServerResponses.onError(error, flow));
    }

    public Mono<ServerResponse> eliminarProductoDeSucursal(ServerRequest serverRequest) {
        int sucursalId;
        int productoId;
        try {
            sucursalId = Integer.parseInt(serverRequest.pathVariable("sucursalId"));
            productoId = Integer.parseInt(serverRequest.pathVariable("productoId"));
        }
        catch (NumberFormatException e) { return ServerResponses.onError(e, null); }

        return franchiseUseCase.eliminarProductoDeSucursal(sucursalId, productoId)
                .flatMap(response -> ServerResponse.ok().bodyValue(response))
                .onErrorResume(error -> ServerResponses.onError(error, null));
    }

    public Mono<ServerResponse> modificarStock(ServerRequest serverRequest) {
        Mono<ModificarStockRequest> flow = serverRequest.bodyToMono(ModificarStockRequest.class).cache();

        int sucursalId;
        int productoId;
        try {
            sucursalId = Integer.parseInt(serverRequest.pathVariable("sucursalId"));
            productoId = Integer.parseInt(serverRequest.pathVariable("productoId"));
        }
        catch (NumberFormatException e) { return ServerResponses.onError(e, null); }

        return flow
                .flatMap(req ->
                        franchiseUseCase.modificarStock(sucursalId, productoId, req.getStock()))
                .flatMap(response -> ServerResponse.ok().bodyValue(response))
                .onErrorResume(error -> ServerResponses.onError(error, null));
    }

    public Mono<ServerResponse> topPerSucursal(ServerRequest serverRequest) {
        int franquiciaId;
        try { franquiciaId = Integer.parseInt(serverRequest.pathVariable("franquiciaId")); }
        catch (NumberFormatException e) { return ServerResponses.onError(e, null); }

        return franchiseUseCase.topPerSucursal(franquiciaId)
                .flatMap(response -> ServerResponse.ok().bodyValue(response))
                .onErrorResume(error -> ServerResponses.onError(error, null));
    }

    public Mono<ServerResponse> modificarFranquicia(ServerRequest serverRequest) {
        int franquiciaId;
        try { franquiciaId = Integer.parseInt(serverRequest.pathVariable("franquiciaId")); }
        catch (NumberFormatException e) { return ServerResponses.onError(e, null); }

        Mono<CrearFranquiciaRequest> flow = serverRequest.bodyToMono(CrearFranquiciaRequest.class).cache();

        return flow
                .flatMap(req -> franchiseUseCase.modificarFranquicia(franquiciaId, mappings.toFranquicia(req)))
                .flatMap(response -> ServerResponse.ok().bodyValue(response))
                .onErrorResume(error -> ServerResponses.onError(error, null));
    }

    public Mono<ServerResponse> modificarProducto(ServerRequest serverRequest) {
        int productoId;
        try { productoId = Integer.parseInt(serverRequest.pathVariable("productoId")); }
        catch (NumberFormatException e) { return ServerResponses.onError(e, null); }

        Mono<ModificarProductoRequest> flow = serverRequest.bodyToMono(ModificarProductoRequest.class).cache();

        return flow
                .flatMap(req -> franchiseUseCase.modificarProducto(productoId, mappings.toProductoMod(req)))
                .flatMap(response -> ServerResponse.ok().bodyValue(response))
                .onErrorResume(error -> ServerResponses.onError(error, null));
    }
}
