package com.andressierra.model.client.gateways;

import com.andressierra.model.client.Client;
import reactor.core.publisher.Mono;

public interface ClientRepository {
    Mono<Client> findById(String id);
    Mono<Client> save(Client client);
}
