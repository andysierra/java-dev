package co.com.asierra.model.sucursal.gateways;

import co.com.asierra.model.sucursal.Sucursal;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SucursalRepository {
    Mono<Sucursal> save(Sucursal sucursal);
    Mono<Sucursal> findById(Integer id);
    Flux<Sucursal> findByExample(Sucursal sucursal);
}
