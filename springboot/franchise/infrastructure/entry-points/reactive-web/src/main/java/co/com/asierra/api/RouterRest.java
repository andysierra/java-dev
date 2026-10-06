package co.com.asierra.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.PATCH;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {
    @Bean
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(POST("/franchise/createFranquicia"), handler::crearFranquicia)
                .andRoute(POST("/franchise/createSucursal"), handler::crearSucursal)
                .andRoute(POST("/franchise/createProducto"), handler::crearProducto)
                .andRoute(DELETE("/franchise/eliminarProductoDeSucursal/{sucursalId}/{productoId}"), handler::eliminarProductoDeSucursal)
                .andRoute(PATCH("/franchise/modificarStock/{sucursalId}/{productoId}"), handler::modificarStock)
                .andRoute(GET("/franchise/topPerSucursal/{franquiciaId}"), handler::topPerSucursal)
                .andRoute(PATCH("/franchise/modificarFranquicia/{franquiciaId}"), handler::modificarFranquicia)
                .andRoute(PATCH("/franchise/modificarProducto/{productoId}"), handler::modificarProducto);
    }
}
