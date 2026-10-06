package com.andressierra.usecase.cancel;

import com.andressierra.model.client.Client;
import com.andressierra.model.client.gateways.ClientRepository;
import com.andressierra.model.exception.BusinessException;
import com.andressierra.model.exception.OptimisticLockException;
import com.andressierra.model.fund.Fund;
import com.andressierra.model.fund.gateways.FundRepository;
import com.andressierra.model.messages.MessagesEnum;
import com.andressierra.model.transaction.Transaction;
import com.andressierra.model.transaction.enums.TransactionTypeEnum;
import com.andressierra.model.transaction.gateways.TransactionRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
public class CancelSubscriptionUseCase {

    private static final int MAX_RETRIES = 3;

    private final ClientRepository clientRepository;
    private final FundRepository fundRepository;
    private final TransactionRepository transactionRepository;

    public Mono<Transaction> cancel(CancelCommand command) {
        return executeCancellation(command)
                .retryWhen(Retry.max(MAX_RETRIES)
                        .filter(OptimisticLockException.class::isInstance));
    }

    private Mono<Transaction> executeCancellation(CancelCommand command) {
        return clientRepository.findById(command.getClientId())
                .switchIfEmpty(Mono.error(BusinessException.fromMessage(MessagesEnum.CLIENT_NOT_FOUND)))
                .flatMap(client -> fundRepository.findById(command.getFundId())
                        .switchIfEmpty(Mono.error(BusinessException.fromMessage(MessagesEnum.FUND_NOT_FOUND)))
                        .flatMap(fund -> transactionRepository
                                .findLastByClientIdAndFundId(client.getId(), fund.getId())
                                .filter(lastTx -> TransactionTypeEnum.SUBSCRIBE.equals(lastTx.getType()))
                                .switchIfEmpty(Mono.error(BusinessException.fromMessage(MessagesEnum.NOT_SUBSCRIBED)))
                                .flatMap(subscription -> performCancellation(client, fund))
                        )
                );
    }

    private Mono<Transaction> performCancellation(Client client, Fund fund) {
        client.setBalance(client.getBalance().add(fund.getMinimumAmount()));

        return clientRepository.save(client)
                .then(Mono.defer(() -> transactionRepository.save(createTransaction(client, fund))));
    }

    private Transaction createTransaction(Client client, Fund fund) {
        return Transaction.builder()
                .id(UUID.randomUUID().toString())
                .clientId(client.getId())
                .fundId(fund.getId())
                .fundName(fund.getName())
                .type(TransactionTypeEnum.CANCEL)
                .amount(fund.getMinimumAmount())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
