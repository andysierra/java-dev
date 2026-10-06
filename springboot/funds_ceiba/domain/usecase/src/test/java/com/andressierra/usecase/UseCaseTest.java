package com.andressierra.usecase;

import com.andressierra.model.client.Client;
import com.andressierra.model.client.enums.NotificationPreferenceEnum;
import com.andressierra.model.client.gateways.ClientRepository;
import com.andressierra.model.exception.BusinessException;
import com.andressierra.model.exception.OptimisticLockException;
import com.andressierra.model.fund.Fund;
import com.andressierra.model.fund.enums.FundCategoryEnum;
import com.andressierra.model.fund.gateways.FundRepository;
import com.andressierra.model.messages.MessagesEnum;
import com.andressierra.model.notification.NotificationGateway;
import com.andressierra.model.transaction.Transaction;
import com.andressierra.model.transaction.enums.TransactionTypeEnum;
import com.andressierra.model.transaction.gateways.TransactionRepository;
import com.andressierra.usecase.cancel.CancelCommand;
import com.andressierra.usecase.cancel.CancelSubscriptionUseCase;
import com.andressierra.usecase.getfunds.GetFundsUseCase;
import com.andressierra.usecase.gettransactions.GetTransactionHistoryUseCase;
import com.andressierra.usecase.subscribe.SubscribeCommand;
import com.andressierra.usecase.subscribe.SubscribeToFundUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UseCaseTest {

    @Mock private ClientRepository clientRepository;
    @Mock private FundRepository fundRepository;
    @Mock private TransactionRepository transactionRepository;
    @Mock private NotificationGateway notificationGateway;

    private SubscribeToFundUseCase subscribeUseCase;
    private CancelSubscriptionUseCase cancelUseCase;
    private GetTransactionHistoryUseCase historyUseCase;
    private GetFundsUseCase fundsUseCase;

    private Client client;
    private Fund fund;

    @BeforeEach
    void setUp() {
        subscribeUseCase = new SubscribeToFundUseCase(clientRepository, fundRepository, transactionRepository, notificationGateway);
        cancelUseCase = new CancelSubscriptionUseCase(clientRepository, fundRepository, transactionRepository);
        historyUseCase = new GetTransactionHistoryUseCase(transactionRepository);
        fundsUseCase = new GetFundsUseCase(fundRepository);

        client = Client.builder()
                .id("CLI-001")
                .name("Andres")
                .email("andres@test.com")
                .phone("3001234567")
                .balance(new BigDecimal("500000"))
                .notificationPreference(NotificationPreferenceEnum.EMAIL)
                .build();

        fund = Fund.builder()
                .id(1L)
                .name("FPV_BTG_PACTUAL_RECAUDADORA")
                .minimumAmount(new BigDecimal("75000"))
                .category(FundCategoryEnum.FPV)
                .build();
    }

    @Nested
    class SubscribeToFund {

        @Test
        void shouldSubscribeSuccessfully() {
            when(clientRepository.findById("CLI-001")).thenReturn(Mono.just(client));
            when(fundRepository.findById(1L)).thenReturn(Mono.just(fund));
            when(transactionRepository.findLastByClientIdAndFundId("CLI-001", 1L)).thenReturn(Mono.empty());
            when(clientRepository.save(any(Client.class))).thenReturn(Mono.just(client));
            when(transactionRepository.save(any(Transaction.class))).thenAnswer(inv -> Mono.just(inv.getArgument(0)));
            when(notificationGateway.notify(any(Client.class), any(Fund.class))).thenReturn(Mono.empty());

            var command = SubscribeCommand.builder()
                    .clientId("CLI-001").fundId(1L).notificationPreference("EMAIL").build();

            StepVerifier.create(subscribeUseCase.subscribe(command))
                    .assertNext(tx -> {
                        assertThat(tx.getClientId()).isEqualTo("CLI-001");
                        assertThat(tx.getFundId()).isEqualTo(1L);
                        assertThat(tx.getType()).isEqualTo(TransactionTypeEnum.SUBSCRIBE);
                        assertThat(tx.getAmount()).isEqualByComparingTo(new BigDecimal("75000"));
                    })
                    .verifyComplete();

            assertThat(client.getBalance()).isEqualByComparingTo(new BigDecimal("425000"));
        }

        @Test
        void shouldSubscribeAfterCancellation() {
            var cancelTx = Transaction.builder()
                    .id("tx-cancel")
                    .clientId("CLI-001").fundId(1L).fundName(fund.getName())
                    .type(TransactionTypeEnum.CANCEL)
                    .amount(fund.getMinimumAmount())
                    .createdAt(LocalDateTime.now())
                    .build();

            when(clientRepository.findById("CLI-001")).thenReturn(Mono.just(client));
            when(fundRepository.findById(1L)).thenReturn(Mono.just(fund));
            when(transactionRepository.findLastByClientIdAndFundId("CLI-001", 1L)).thenReturn(Mono.just(cancelTx));
            when(clientRepository.save(any(Client.class))).thenReturn(Mono.just(client));
            when(transactionRepository.save(any(Transaction.class))).thenAnswer(inv -> Mono.just(inv.getArgument(0)));
            when(notificationGateway.notify(any(Client.class), any(Fund.class))).thenReturn(Mono.empty());

            var command = SubscribeCommand.builder()
                    .clientId("CLI-001").fundId(1L).notificationPreference("SMS").build();

            StepVerifier.create(subscribeUseCase.subscribe(command))
                    .assertNext(tx -> assertThat(tx.getType()).isEqualTo(TransactionTypeEnum.SUBSCRIBE))
                    .verifyComplete();
        }

        @Test
        void shouldFailWhenClientNotFound() {
            when(clientRepository.findById("CLI-999")).thenReturn(Mono.empty());

            var command = SubscribeCommand.builder()
                    .clientId("CLI-999").fundId(1L).notificationPreference("EMAIL").build();

            StepVerifier.create(subscribeUseCase.subscribe(command))
                    .expectErrorMatches(e -> e instanceof BusinessException
                            && ((BusinessException) e).getOpcode().equals(MessagesEnum.CLIENT_NOT_FOUND.getOperationCode()))
                    .verify();
        }

        @Test
        void shouldFailWhenFundNotFound() {
            when(clientRepository.findById("CLI-001")).thenReturn(Mono.just(client));
            when(fundRepository.findById(999L)).thenReturn(Mono.empty());

            var command = SubscribeCommand.builder()
                    .clientId("CLI-001").fundId(999L).notificationPreference("EMAIL").build();

            StepVerifier.create(subscribeUseCase.subscribe(command))
                    .expectErrorMatches(e -> e instanceof BusinessException
                            && ((BusinessException) e).getOpcode().equals(MessagesEnum.FUND_NOT_FOUND.getOperationCode()))
                    .verify();
        }

        @Test
        void shouldFailWhenAlreadySubscribed() {
            var subscribeTx = Transaction.builder()
                    .id("tx-sub")
                    .clientId("CLI-001").fundId(1L).fundName(fund.getName())
                    .type(TransactionTypeEnum.SUBSCRIBE)
                    .amount(fund.getMinimumAmount())
                    .createdAt(LocalDateTime.now())
                    .build();

            when(clientRepository.findById("CLI-001")).thenReturn(Mono.just(client));
            when(fundRepository.findById(1L)).thenReturn(Mono.just(fund));
            when(transactionRepository.findLastByClientIdAndFundId("CLI-001", 1L)).thenReturn(Mono.just(subscribeTx));

            var command = SubscribeCommand.builder()
                    .clientId("CLI-001").fundId(1L).notificationPreference("EMAIL").build();

            StepVerifier.create(subscribeUseCase.subscribe(command))
                    .expectErrorMatches(e -> e instanceof BusinessException
                            && ((BusinessException) e).getOpcode().equals(MessagesEnum.ALREADY_SUBSCRIBED.getOperationCode()))
                    .verify();

            verify(clientRepository, never()).save(any());
        }

        @Test
        void shouldFailWhenInsufficientBalance() {
            client.setBalance(new BigDecimal("10000"));

            when(clientRepository.findById("CLI-001")).thenReturn(Mono.just(client));
            when(fundRepository.findById(1L)).thenReturn(Mono.just(fund));
            when(transactionRepository.findLastByClientIdAndFundId("CLI-001", 1L)).thenReturn(Mono.empty());

            var command = SubscribeCommand.builder()
                    .clientId("CLI-001").fundId(1L).notificationPreference("EMAIL").build();

            StepVerifier.create(subscribeUseCase.subscribe(command))
                    .expectErrorMatches(e -> e instanceof BusinessException
                            && ((BusinessException) e).getOpcode().equals(MessagesEnum.INSUFFICIENT_BALANCE.getOperationCode()))
                    .verify();

            verify(clientRepository, never()).save(any());
        }

        @Test
        void shouldFailWhenInvalidNotificationPreference() {
            when(clientRepository.findById("CLI-001")).thenReturn(Mono.just(client));
            when(fundRepository.findById(1L)).thenReturn(Mono.just(fund));
            when(transactionRepository.findLastByClientIdAndFundId("CLI-001", 1L)).thenReturn(Mono.empty());

            var command = SubscribeCommand.builder()
                    .clientId("CLI-001").fundId(1L).notificationPreference("WHATSAPP").build();

            StepVerifier.create(subscribeUseCase.subscribe(command))
                    .expectErrorMatches(e -> e instanceof BusinessException
                            && ((BusinessException) e).getOpcode().equals(MessagesEnum.BAD_REQUEST.getOperationCode()))
                    .verify();

            verify(clientRepository, never()).save(any());
        }

        @Test
        void shouldRetryOnOptimisticLockConflict() {
            var counter = new AtomicInteger(0);
            when(clientRepository.findById("CLI-001")).thenReturn(Mono.just(client));
            when(fundRepository.findById(1L)).thenReturn(Mono.just(fund));
            when(transactionRepository.findLastByClientIdAndFundId("CLI-001", 1L)).thenReturn(Mono.empty());
            when(clientRepository.save(any(Client.class))).thenAnswer(inv -> {
                if (counter.getAndIncrement() == 0) {
                    return Mono.error(new OptimisticLockException());
                }
                return Mono.just(inv.getArgument(0));
            });
            when(transactionRepository.save(any(Transaction.class))).thenAnswer(inv -> Mono.just(inv.getArgument(0)));
            when(notificationGateway.notify(any(Client.class), any(Fund.class))).thenReturn(Mono.empty());

            var command = SubscribeCommand.builder()
                    .clientId("CLI-001").fundId(1L).notificationPreference("EMAIL").build();

            StepVerifier.create(subscribeUseCase.subscribe(command))
                    .assertNext(tx -> assertThat(tx.getType()).isEqualTo(TransactionTypeEnum.SUBSCRIBE))
                    .verifyComplete();

            verify(clientRepository, times(2)).save(any(Client.class));
        }

        @Test
        void shouldFailAfterMaxRetriesOnPersistentConflict() {
            when(clientRepository.findById("CLI-001")).thenReturn(Mono.just(client));
            when(fundRepository.findById(1L)).thenReturn(Mono.just(fund));
            when(transactionRepository.findLastByClientIdAndFundId("CLI-001", 1L)).thenReturn(Mono.empty());
            when(clientRepository.save(any(Client.class)))
                    .thenReturn(Mono.error(new OptimisticLockException()));

            var command = SubscribeCommand.builder()
                    .clientId("CLI-001").fundId(1L).notificationPreference("EMAIL").build();

            StepVerifier.create(subscribeUseCase.subscribe(command))
                    .expectErrorMatches(e -> Exceptions.isRetryExhausted(e)
                            && e.getCause() instanceof OptimisticLockException)
                    .verify();
        }
    }

    @Nested
    class CancelSubscription {

        @Test
        void shouldCancelSuccessfully() {
            var subscribeTx = Transaction.builder()
                    .id("tx-sub")
                    .clientId("CLI-001").fundId(1L).fundName(fund.getName())
                    .type(TransactionTypeEnum.SUBSCRIBE)
                    .amount(fund.getMinimumAmount())
                    .createdAt(LocalDateTime.now())
                    .build();

            when(clientRepository.findById("CLI-001")).thenReturn(Mono.just(client));
            when(fundRepository.findById(1L)).thenReturn(Mono.just(fund));
            when(transactionRepository.findLastByClientIdAndFundId("CLI-001", 1L)).thenReturn(Mono.just(subscribeTx));
            when(clientRepository.save(any(Client.class))).thenReturn(Mono.just(client));
            when(transactionRepository.save(any(Transaction.class))).thenAnswer(inv -> Mono.just(inv.getArgument(0)));

            var command = CancelCommand.builder().clientId("CLI-001").fundId(1L).build();

            StepVerifier.create(cancelUseCase.cancel(command))
                    .assertNext(tx -> {
                        assertThat(tx.getType()).isEqualTo(TransactionTypeEnum.CANCEL);
                        assertThat(tx.getAmount()).isEqualByComparingTo(new BigDecimal("75000"));
                    })
                    .verifyComplete();

            assertThat(client.getBalance()).isEqualByComparingTo(new BigDecimal("575000"));
        }

        @Test
        void shouldFailWhenNotSubscribed() {
            when(clientRepository.findById("CLI-001")).thenReturn(Mono.just(client));
            when(fundRepository.findById(1L)).thenReturn(Mono.just(fund));
            when(transactionRepository.findLastByClientIdAndFundId("CLI-001", 1L)).thenReturn(Mono.empty());

            var command = CancelCommand.builder().clientId("CLI-001").fundId(1L).build();

            StepVerifier.create(cancelUseCase.cancel(command))
                    .expectErrorMatches(e -> e instanceof BusinessException
                            && ((BusinessException) e).getOpcode().equals(MessagesEnum.NOT_SUBSCRIBED.getOperationCode()))
                    .verify();
        }

        @Test
        void shouldFailWhenLastTransactionIsCancel() {
            var cancelTx = Transaction.builder()
                    .id("tx-cancel")
                    .clientId("CLI-001").fundId(1L).fundName(fund.getName())
                    .type(TransactionTypeEnum.CANCEL)
                    .amount(fund.getMinimumAmount())
                    .createdAt(LocalDateTime.now())
                    .build();

            when(clientRepository.findById("CLI-001")).thenReturn(Mono.just(client));
            when(fundRepository.findById(1L)).thenReturn(Mono.just(fund));
            when(transactionRepository.findLastByClientIdAndFundId("CLI-001", 1L)).thenReturn(Mono.just(cancelTx));

            var command = CancelCommand.builder().clientId("CLI-001").fundId(1L).build();

            StepVerifier.create(cancelUseCase.cancel(command))
                    .expectErrorMatches(e -> e instanceof BusinessException
                            && ((BusinessException) e).getOpcode().equals(MessagesEnum.NOT_SUBSCRIBED.getOperationCode()))
                    .verify();

            verify(clientRepository, never()).save(any());
        }

        @Test
        void shouldFailWhenClientNotFound() {
            when(clientRepository.findById("CLI-999")).thenReturn(Mono.empty());

            var command = CancelCommand.builder().clientId("CLI-999").fundId(1L).build();

            StepVerifier.create(cancelUseCase.cancel(command))
                    .expectErrorMatches(e -> e instanceof BusinessException
                            && ((BusinessException) e).getOpcode().equals(MessagesEnum.CLIENT_NOT_FOUND.getOperationCode()))
                    .verify();
        }

        @Test
        void shouldFailWhenFundNotFound() {
            when(clientRepository.findById("CLI-001")).thenReturn(Mono.just(client));
            when(fundRepository.findById(999L)).thenReturn(Mono.empty());

            var command = CancelCommand.builder().clientId("CLI-001").fundId(999L).build();

            StepVerifier.create(cancelUseCase.cancel(command))
                    .expectErrorMatches(e -> e instanceof BusinessException
                            && ((BusinessException) e).getOpcode().equals(MessagesEnum.FUND_NOT_FOUND.getOperationCode()))
                    .verify();
        }

        @Test
        void shouldRetryOnOptimisticLockConflict() {
            var subscribeTx = Transaction.builder()
                    .id("tx-sub").clientId("CLI-001").fundId(1L).fundName(fund.getName())
                    .type(TransactionTypeEnum.SUBSCRIBE).amount(fund.getMinimumAmount())
                    .createdAt(LocalDateTime.now()).build();

            var counter = new AtomicInteger(0);
            when(clientRepository.findById("CLI-001")).thenReturn(Mono.just(client));
            when(fundRepository.findById(1L)).thenReturn(Mono.just(fund));
            when(transactionRepository.findLastByClientIdAndFundId("CLI-001", 1L)).thenReturn(Mono.just(subscribeTx));
            when(clientRepository.save(any(Client.class))).thenAnswer(inv -> {
                if (counter.getAndIncrement() == 0) {
                    return Mono.error(new OptimisticLockException());
                }
                return Mono.just(inv.getArgument(0));
            });
            when(transactionRepository.save(any(Transaction.class))).thenAnswer(inv -> Mono.just(inv.getArgument(0)));

            var command = CancelCommand.builder().clientId("CLI-001").fundId(1L).build();

            StepVerifier.create(cancelUseCase.cancel(command))
                    .assertNext(tx -> assertThat(tx.getType()).isEqualTo(TransactionTypeEnum.CANCEL))
                    .verifyComplete();

            verify(clientRepository, times(2)).save(any(Client.class));
        }
    }

    @Nested
    class GetTransactionHistory {

        @Test
        void shouldReturnTransactions() {
            var tx1 = Transaction.builder()
                    .id("tx-1").clientId("CLI-001").fundId(1L).fundName("Fund A")
                    .type(TransactionTypeEnum.SUBSCRIBE).amount(new BigDecimal("75000"))
                    .createdAt(LocalDateTime.now())
                    .build();
            var tx2 = Transaction.builder()
                    .id("tx-2").clientId("CLI-001").fundId(2L).fundName("Fund B")
                    .type(TransactionTypeEnum.CANCEL).amount(new BigDecimal("100000"))
                    .createdAt(LocalDateTime.now())
                    .build();

            when(transactionRepository.findByClientId("CLI-001")).thenReturn(Flux.just(tx1, tx2));

            StepVerifier.create(historyUseCase.getByClientId("CLI-001"))
                    .expectNext(tx1)
                    .expectNext(tx2)
                    .verifyComplete();
        }

        @Test
        void shouldReturnEmptyWhenNoTransactions() {
            when(transactionRepository.findByClientId("CLI-001")).thenReturn(Flux.empty());

            StepVerifier.create(historyUseCase.getByClientId("CLI-001"))
                    .verifyComplete();
        }
    }

    @Nested
    class GetFunds {

        @Test
        void shouldReturnAllFunds() {
            var fund2 = Fund.builder()
                    .id(2L).name("FPV_BTG_PACTUAL_ECOPETROL")
                    .minimumAmount(new BigDecimal("75000"))
                    .category(FundCategoryEnum.FPV)
                    .build();

            when(fundRepository.findAll()).thenReturn(Flux.just(fund, fund2));

            StepVerifier.create(fundsUseCase.getAll())
                    .expectNext(fund)
                    .expectNext(fund2)
                    .verifyComplete();
        }

        @Test
        void shouldReturnEmptyWhenNoFunds() {
            when(fundRepository.findAll()).thenReturn(Flux.empty());

            StepVerifier.create(fundsUseCase.getAll())
                    .verifyComplete();
        }
    }
}
