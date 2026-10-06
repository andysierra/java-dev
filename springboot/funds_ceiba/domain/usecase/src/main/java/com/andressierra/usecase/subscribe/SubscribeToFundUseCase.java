package com.andressierra.usecase.subscribe;

import com.andressierra.model.client.Client;
import com.andressierra.model.client.enums.NotificationPreferenceEnum;
import com.andressierra.model.client.gateways.ClientRepository;
import com.andressierra.model.exception.BusinessException;
import com.andressierra.model.exception.OptimisticLockException;
import com.andressierra.model.fund.Fund;
import com.andressierra.model.fund.gateways.FundRepository;
import com.andressierra.model.messages.MessagesEnum;
import com.andressierra.model.notification.NotificationGateway;
import com.andressierra.model.transaction.Transaction;
import com.andressierra.model.transaction.enums.TransactionTypeEnum;
import com.andressierra.model.transaction.gateways.TransactionRepository;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
public class SubscribeToFundUseCase {

    private static final int MAX_RETRIES = 3;

    private final ClientRepository clientRepository;
    private final FundRepository fundRepository;
    private final TransactionRepository transactionRepository;
    private final NotificationGateway notificationGateway;

    public Mono<Transaction> subscribe(SubscribeCommand command) {
        return executeSubscription(command)
                .retryWhen(Retry.max(MAX_RETRIES)
                        .filter(OptimisticLockException.class::isInstance));
    }

    private Mono<Transaction> executeSubscription(SubscribeCommand command) {
        return clientRepository.findById(command.getClientId())
                .switchIfEmpty(Mono.error(BusinessException.fromMessage(MessagesEnum.CLIENT_NOT_FOUND)))
                .flatMap(client -> fundRepository.findById(command.getFundId())
                        .switchIfEmpty(Mono.error(BusinessException.fromMessage(MessagesEnum.FUND_NOT_FOUND)))
                        .flatMap(fund -> transactionRepository
                                .findLastByClientIdAndFundId(client.getId(), fund.getId())
                                .flatMap(lastTx -> {
                                    if (TransactionTypeEnum.SUBSCRIBE.equals(lastTx.getType())) {
                                        return Mono.<Transaction>error(BusinessException.fromMessage(MessagesEnum.ALREADY_SUBSCRIBED));
                                    }
                                    return Mono.<Transaction>empty();
                                })
                                .switchIfEmpty(Mono.defer(() -> {
                                    hasEnoughCredit(client.getBalance(), fund);
                                    isValidNotificationPreference(command.getNotificationPreference());
                                    client.setBalance(client.getBalance().subtract(fund.getMinimumAmount()));

                                    return clientRepository.save(client)
                                            .then(Mono.defer(() -> transactionRepository.save(createTransaction(client, fund))))
                                            .flatMap(savedTx -> notificationGateway.notify(client, fund)
                                                    .thenReturn(savedTx));
                                }))
                        )
                );
    }

    private Transaction createTransaction(Client client, Fund fund) {
        return Transaction.builder()
                .id(UUID.randomUUID().toString())
                .clientId(client.getId())
                .fundId(fund.getId())
                .fundName(fund.getName())
                .type(TransactionTypeEnum.SUBSCRIBE)
                .amount(fund.getMinimumAmount())
                .createdAt(LocalDateTime.now())
                .build();
    }

    private void hasEnoughCredit(BigDecimal clientBalance, Fund fund) {
        if (clientBalance.compareTo(fund.getMinimumAmount()) < 0) {
            throw new BusinessException(
                    "No tiene saldo disponible para vincularse al fondo " + fund.getName(),
                    MessagesEnum.INSUFFICIENT_BALANCE.getOperationCode(),
                    MessagesEnum.INSUFFICIENT_BALANCE.getCode()
            );
        }
    }

    private void isValidNotificationPreference(String preference) {
        try {
            NotificationPreferenceEnum.valueOf(preference);
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new BusinessException(
                    "Preferencia de notificacion invalida, debe ser EMAIL o SMS",
                    MessagesEnum.BAD_REQUEST.getOperationCode(),
                    MessagesEnum.BAD_REQUEST.getCode()
            );
        }
    }
}
