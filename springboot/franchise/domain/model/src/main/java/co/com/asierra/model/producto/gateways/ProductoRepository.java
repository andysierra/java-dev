package co.com.asierra.model.producto.gateways;

import co.com.asierra.model.producto.Producto;
import reactor.core.publisher.Mono;

public interface ProductoRepository {
    Mono<Producto> save(Producto producto);
    Mono<Producto> findById(Integer id);
    Mono<Producto> findOne(Producto entity);
    Mono<Void> deleteById(Integer id);
}
