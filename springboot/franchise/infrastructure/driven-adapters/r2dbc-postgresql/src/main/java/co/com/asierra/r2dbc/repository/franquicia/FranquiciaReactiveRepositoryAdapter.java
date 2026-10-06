package co.com.asierra.r2dbc.repository.franquicia;

import co.com.asierra.model.franquicia.Franquicia;
import co.com.asierra.model.franquicia.gateways.FranquiciaRepository;
import co.com.asierra.r2dbc.data.FranquiciaData;
import co.com.asierra.r2dbc.exceptions.FranquiciaNoEncontradaException;
import co.com.asierra.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class FranquiciaReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        Franquicia/* change for domain model */,
        FranquiciaData/* change for adapter model */,
        Integer,
        FranquiciaReactiveRepository> implements FranquiciaRepository
{
    public FranquiciaReactiveRepositoryAdapter(FranquiciaReactiveRepository repository, ObjectMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, d -> mapper.map(d, Franquicia.class/* change for domain model */));
    }

    @Override
    public Mono<Franquicia> findById(Integer id) {
        return super.findById(id)
                .switchIfEmpty(Mono.error(new FranquiciaNoEncontradaException(String.format("La franquicia [%d] no existe", id))));
    }
}
