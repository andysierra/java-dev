package com.andressierra.model.fund.gateways;

import com.andressierra.model.fund.Fund;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FundRepository {
    Mono<Fund> findById(Long id);
    Flux<Fund> findAll();
}
