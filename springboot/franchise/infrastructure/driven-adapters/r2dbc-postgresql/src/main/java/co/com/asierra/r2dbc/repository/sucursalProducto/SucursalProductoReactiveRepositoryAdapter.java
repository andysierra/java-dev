package co.com.asierra.r2dbc.repository.sucursalProducto;

import co.com.asierra.model.sucursalproducto.SucursalProducto;
import co.com.asierra.model.sucursalproducto.gateways.SucursalProductoRepository;
import co.com.asierra.r2dbc.data.SucursalProductoData;
import co.com.asierra.r2dbc.exceptions.SucursalNoEncontradaException;
import co.com.asierra.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class SucursalProductoReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        SucursalProducto/* change for domain model */,
        SucursalProductoData/* change for adapter model */,
        Void,
        co.com.asierra.r2dbc.repository.sucursalProducto.SucursalProductoReactiveRepository> implements SucursalProductoRepository
{
    public SucursalProductoReactiveRepositoryAdapter(SucursalProductoReactiveRepository repository, ObjectMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, d -> mapper.map(d, SucursalProducto.class/* change for domain model */));
    }

    @Override
    public Mono<SucursalProducto> findOne(SucursalProducto sucursalProducto) {
        return repository.findOne(Example.of(toData(sucursalProducto)))
                .map(this::toEntity);
    }

    public Mono<Long> countAllBySucursalIdAndProductoId(Integer sucursalId, Integer productoId) {
        return repository.countAllBySucursalIdAndProductoId(sucursalId, productoId);
    }
}
