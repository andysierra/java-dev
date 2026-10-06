package co.com.asierra.r2dbc.repository.sucursal;

import co.com.asierra.model.sucursal.Sucursal;
import co.com.asierra.model.sucursal.gateways.SucursalRepository;
import co.com.asierra.r2dbc.data.SucursalData;
import co.com.asierra.r2dbc.exceptions.SucursalNoEncontradaException;
import co.com.asierra.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class SucursalReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        Sucursal/* change for domain model */,
        SucursalData/* change for adapter model */,
        Integer,
        co.com.asierra.r2dbc.repository.sucursal.SucursalReactiveRepository> implements SucursalRepository
{
    public SucursalReactiveRepositoryAdapter(SucursalReactiveRepository repository, ObjectMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, d -> mapper.map(d, Sucursal.class/* change for domain model */));
    }


    @Override
    public Mono<Sucursal> findById(Integer id) {
        return super.findById(id)
                .switchIfEmpty(Mono.error(new SucursalNoEncontradaException(String.format("La sucursal [%d] no existe", id))));
    }

    @Override
    public Flux<Sucursal> findByExample(Sucursal sucursal) {
        return super.findByExample(sucursal);
    }
}
