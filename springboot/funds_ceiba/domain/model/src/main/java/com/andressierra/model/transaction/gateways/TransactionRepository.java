package com.andressierra.model.transaction.gateways;

import com.andressierra.model.transaction.Transaction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TransactionRepository {
    Mono<Transaction> save(Transaction transaction);
    Flux<Transaction> findByClientId(String clientId);
    Mono<Transaction> findLastByClientIdAndFundId(String clientId, Long fundId);
}
