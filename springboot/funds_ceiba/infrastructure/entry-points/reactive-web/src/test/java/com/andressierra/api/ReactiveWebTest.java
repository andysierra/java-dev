package com.andressierra.api;

import com.andressierra.model.exception.BusinessException;
import com.andressierra.model.fund.Fund;
import com.andressierra.model.fund.enums.FundCategoryEnum;
import com.andressierra.model.messages.MessagesEnum;
import com.andressierra.model.transaction.Transaction;
import com.andressierra.model.transaction.enums.TransactionTypeEnum;
import com.andressierra.usecase.cancel.CancelCommand;
import com.andressierra.usecase.cancel.CancelSubscriptionUseCase;
import com.andressierra.usecase.getfunds.GetFundsUseCase;
import com.andressierra.usecase.gettransactions.GetTransactionHistoryUseCase;
import com.andressierra.usecase.subscribe.SubscribeCommand;
import com.andressierra.usecase.subscribe.SubscribeToFundUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.HttpMethod;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReactiveWebTest {

    @Mock private SubscribeToFundUseCase subscribeUseCase;
    @Mock private CancelSubscriptionUseCase cancelUseCase;
    @Mock private GetTransactionHistoryUseCase historyUseCase;
    @Mock private GetFundsUseCase fundsUseCase;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        var handler = new Handler(subscribeUseCase, cancelUseCase, historyUseCase, fundsUseCase);
        var routerRest = new RouterRest();
        webTestClient = WebTestClient.bindToRouterFunction(routerRest.routerFunction(handler)).build();
    }

    @Nested
    class Subscribe {

        @Test
        void shouldSubscribeSuccessfully() {
            var tx = buildTransaction(TransactionTypeEnum.SUBSCRIBE);
            when(subscribeUseCase.subscribe(any(SubscribeCommand.class))).thenReturn(Mono.just(tx));

            webTestClient.post().uri("/api/v1/subscriptions")
                    .bodyValue("""
                            {"clientId":"CLI-001","fundId":1,"notificationPreference":"EMAIL"}
                            """)
                    .header("Content-Type", "application/json")
                    .exchange()
                    .expectStatus().isCreated()
                    .expectBody()
                    .jsonPath("$.code").isEqualTo("21")
                    .jsonPath("$.data.transactionId").isEqualTo("tx-001")
                    .jsonPath("$.data.fundName").isEqualTo("FPV_BTG_PACTUAL_RECAUDADORA")
                    .jsonPath("$.data.amount").isEqualTo(75000);
        }

        @Test
        void shouldReturn400WhenClientIdBlank() {
            webTestClient.post().uri("/api/v1/subscriptions")
                    .bodyValue("""
                            {"clientId":"","fundId":1,"notificationPreference":"EMAIL"}
                            """)
                    .header("Content-Type", "application/json")
                    .exchange()
                    .expectStatus().isBadRequest()
                    .expectBody()
                    .jsonPath("$.code").isEqualTo("40");
        }

        @Test
        void shouldReturn400WhenFundIdNull() {
            webTestClient.post().uri("/api/v1/subscriptions")
                    .bodyValue("""
                            {"clientId":"CLI-001","notificationPreference":"EMAIL"}
                            """)
                    .header("Content-Type", "application/json")
                    .exchange()
                    .expectStatus().isBadRequest()
                    .expectBody()
                    .jsonPath("$.code").isEqualTo("40");
        }

        @Test
        void shouldReturn409WhenAlreadySubscribed() {
            when(subscribeUseCase.subscribe(any(SubscribeCommand.class)))
                    .thenReturn(Mono.error(BusinessException.fromMessage(MessagesEnum.ALREADY_SUBSCRIBED)));

            webTestClient.post().uri("/api/v1/subscriptions")
                    .bodyValue("""
                            {"clientId":"CLI-001","fundId":1,"notificationPreference":"EMAIL"}
                            """)
                    .header("Content-Type", "application/json")
                    .exchange()
                    .expectStatus().isEqualTo(409)
                    .expectBody()
                    .jsonPath("$.code").isEqualTo("44");
        }

        @Test
        void shouldReturn404WhenClientNotFound() {
            when(subscribeUseCase.subscribe(any(SubscribeCommand.class)))
                    .thenReturn(Mono.error(BusinessException.fromMessage(MessagesEnum.CLIENT_NOT_FOUND)));

            webTestClient.post().uri("/api/v1/subscriptions")
                    .bodyValue("""
                            {"clientId":"CLI-999","fundId":1,"notificationPreference":"EMAIL"}
                            """)
                    .header("Content-Type", "application/json")
                    .exchange()
                    .expectStatus().isNotFound()
                    .expectBody()
                    .jsonPath("$.code").isEqualTo("43");
        }

        @Test
        void shouldReturn400WhenInsufficientBalance() {
            when(subscribeUseCase.subscribe(any(SubscribeCommand.class)))
                    .thenReturn(Mono.error(BusinessException.fromMessage(MessagesEnum.INSUFFICIENT_BALANCE)));

            webTestClient.post().uri("/api/v1/subscriptions")
                    .bodyValue("""
                            {"clientId":"CLI-001","fundId":1,"notificationPreference":"EMAIL"}
                            """)
                    .header("Content-Type", "application/json")
                    .exchange()
                    .expectStatus().isBadRequest()
                    .expectBody()
                    .jsonPath("$.code").isEqualTo("41");
        }
    }

    @Nested
    class CancelSubscription {

        @Test
        void shouldCancelSuccessfully() {
            var tx = buildTransaction(TransactionTypeEnum.CANCEL);
            when(cancelUseCase.cancel(any(CancelCommand.class))).thenReturn(Mono.just(tx));

            webTestClient.method(HttpMethod.DELETE).uri("/api/v1/subscriptions")
                    .bodyValue("""
                            {"clientId":"CLI-001","fundId":1}
                            """)
                    .header("Content-Type", "application/json")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .jsonPath("$.code").isEqualTo("22");
        }

        @Test
        void shouldReturn400WhenNotSubscribed() {
            when(cancelUseCase.cancel(any(CancelCommand.class)))
                    .thenReturn(Mono.error(BusinessException.fromMessage(MessagesEnum.NOT_SUBSCRIBED)));

            webTestClient.method(HttpMethod.DELETE).uri("/api/v1/subscriptions")
                    .bodyValue("""
                            {"clientId":"CLI-001","fundId":1}
                            """)
                    .header("Content-Type", "application/json")
                    .exchange()
                    .expectStatus().isBadRequest()
                    .expectBody()
                    .jsonPath("$.code").isEqualTo("45");
        }
    }

    @Nested
    class GetTransactionHistory {

        @Test
        void shouldReturnTransactions() {
            var tx = buildTransaction(TransactionTypeEnum.SUBSCRIBE);
            when(historyUseCase.getByClientId("CLI-001")).thenReturn(Flux.just(tx));

            webTestClient.get().uri("/api/v1/transactions/CLI-001")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .jsonPath("$.code").isEqualTo("23")
                    .jsonPath("$.data[0].transactionId").isEqualTo("tx-001")
                    .jsonPath("$.data[0].type").isEqualTo("SUBSCRIBE");
        }

        @Test
        void shouldReturnEmptyList() {
            when(historyUseCase.getByClientId("CLI-001")).thenReturn(Flux.empty());

            webTestClient.get().uri("/api/v1/transactions/CLI-001")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .jsonPath("$.code").isEqualTo("23")
                    .jsonPath("$.data").isArray();
        }
    }

    @Nested
    class GetFunds {

        @Test
        void shouldReturnFunds() {
            var fund = Fund.builder()
                    .id(1L).name("FPV_BTG_PACTUAL_RECAUDADORA")
                    .minimumAmount(new BigDecimal("75000"))
                    .category(FundCategoryEnum.FPV)
                    .build();

            when(fundsUseCase.getAll()).thenReturn(Flux.just(fund));

            webTestClient.get().uri("/api/v1/funds")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .jsonPath("$.code").isEqualTo("24")
                    .jsonPath("$.data[0].id").isEqualTo(1)
                    .jsonPath("$.data[0].name").isEqualTo("FPV_BTG_PACTUAL_RECAUDADORA");
        }
    }

    private Transaction buildTransaction(TransactionTypeEnum type) {
        return Transaction.builder()
                .id("tx-001")
                .clientId("CLI-001")
                .fundId(1L)
                .fundName("FPV_BTG_PACTUAL_RECAUDADORA")
                .type(type)
                .amount(new BigDecimal("75000"))
                .createdAt(LocalDateTime.of(2026, 3, 16, 10, 0))
                .build();
    }
}
