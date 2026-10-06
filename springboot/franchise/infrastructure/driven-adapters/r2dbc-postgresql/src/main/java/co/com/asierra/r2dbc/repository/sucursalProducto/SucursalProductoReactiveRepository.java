package co.com.asierra.r2dbc.repository.sucursalProducto;

import co.com.asierra.r2dbc.data.SucursalProductoData;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

// TODO: This file is just an example, you should delete or modify it
public interface SucursalProductoReactiveRepository extends ReactiveCrudRepository<SucursalProductoData, Void>, ReactiveQueryByExampleExecutor<SucursalProductoData> {
    Mono<Long> countAllBySucursalIdAndProductoId(Integer sucursalId, Integer productoId);
}
