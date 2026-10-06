package co.com.asierra.model.franquicia.gateways;

import co.com.asierra.model.franquicia.Franquicia;
import reactor.core.publisher.Mono;

public interface FranquiciaRepository {
    Mono<Franquicia> save(Franquicia franquicia);
    Mono<Franquicia> findById(Integer id);
}
