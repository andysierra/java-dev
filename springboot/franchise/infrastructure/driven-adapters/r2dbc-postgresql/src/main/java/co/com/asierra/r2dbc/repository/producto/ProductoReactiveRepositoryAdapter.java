package co.com.asierra.r2dbc.repository.producto;

import co.com.asierra.model.producto.Producto;
import co.com.asierra.model.producto.gateways.ProductoRepository;
import co.com.asierra.r2dbc.data.ProductoData;
import co.com.asierra.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class ProductoReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        Producto,
        ProductoData,
        Integer,
        ProductoReactiveRepository> implements ProductoRepository
{
    public ProductoReactiveRepositoryAdapter(ProductoReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Producto.class));
    }


    @Override
    public Mono<Producto> findById(Integer id) {
        return super.findById(id);
    }

    @Override
    public Mono<Void> deleteById(Integer id) {
        return null;
    }
}
