package com.andressierra.usecase.gettransactions;

import com.andressierra.model.transaction.Transaction;
import com.andressierra.model.transaction.gateways.TransactionRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
public class GetTransactionHistoryUseCase {

    private final TransactionRepository transactionRepository;

    public Flux<Transaction> getByClientId(String clientId) {
        return transactionRepository.findByClientId(clientId);
    }
}
