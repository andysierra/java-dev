package com.andressierra.dynamodb;

import com.andressierra.dynamodb.entity.ClientEntity;
import com.andressierra.dynamodb.entity.FundEntity;
import com.andressierra.dynamodb.entity.TransactionEntity;
import com.andressierra.dynamodb.helper.DynamoDbMapper;
import com.andressierra.model.client.Client;
import com.andressierra.model.client.enums.NotificationPreferenceEnum;
import com.andressierra.model.exception.BusinessException;
import com.andressierra.model.exception.OptimisticLockException;
import com.andressierra.model.fund.Fund;
import com.andressierra.model.fund.enums.FundCategoryEnum;
import com.andressierra.model.transaction.Transaction;
import com.andressierra.model.transaction.enums.TransactionTypeEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbIndex;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.services.dynamodb.model.ConditionalCheckFailedException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DynamoDbAdapterTest {

    @Mock private DynamoDbTable<ClientEntity> clientTable;
    @Mock private DynamoDbTable<FundEntity> fundTable;
    @Mock private DynamoDbTable<TransactionEntity> transactionTable;
    @Mock private DynamoDbIndex<TransactionEntity> clientIdIndex;
    @Mock private DynamoDbIndex<TransactionEntity> clientIdFundIdIndex;

    private ClientRepositoryAdapter clientAdapter;
    private FundRepositoryAdapter fundAdapter;
    private TransactionRepositoryAdapter transactionAdapter;

    @BeforeEach
    void setUp() {
        DynamoDbEnhancedClient clientEnhanced = mock(DynamoDbEnhancedClient.class);
        DynamoDbEnhancedClient fundEnhanced = mock(DynamoDbEnhancedClient.class);
        DynamoDbEnhancedClient txEnhanced = mock(DynamoDbEnhancedClient.class);

        when(clientEnhanced.table(any(), any())).thenReturn((DynamoDbTable) clientTable);
        when(fundEnhanced.table(any(), any())).thenReturn((DynamoDbTable) fundTable);
        when(txEnhanced.table(any(), any())).thenReturn((DynamoDbTable) transactionTable);
        when(transactionTable.index("clientId-index")).thenReturn((DynamoDbIndex) clientIdIndex);
        when(transactionTable.index("clientIdFundId-index")).thenReturn((DynamoDbIndex) clientIdFundIdIndex);

        var props = new com.andressierra.dynamodb.config.DynamoDbProperties("us-east-1", "clients", "funds", "transactions");

        clientAdapter = new ClientRepositoryAdapter(clientEnhanced, props);
        fundAdapter = new FundRepositoryAdapter(fundEnhanced, props);
        transactionAdapter = new TransactionRepositoryAdapter(txEnhanced, props);
    }

    @Nested
    class MapperTests {

        @Test
        void shouldMapClientToEntityAndBack() {
            var client = Client.builder()
                    .id("CLI-001").name("Andres").email("a@test.com").phone("300")
                    .balance(new BigDecimal("500000"))
                    .notificationPreference(NotificationPreferenceEnum.EMAIL)
                    .version(3L)
                    .build();

            var data = DynamoDbMapper.toData(client);
            assertThat(data.getId()).isEqualTo("CLI-001");
            assertThat(data.getNotificationPreference()).isEqualTo("EMAIL");
            assertThat(data.getVersion()).isEqualTo(3L);

            var back = DynamoDbMapper.toEntity(data);
            assertThat(back.getId()).isEqualTo(client.getId());
            assertThat(back.getBalance()).isEqualByComparingTo(client.getBalance());
            assertThat(back.getNotificationPreference()).isEqualTo(NotificationPreferenceEnum.EMAIL);
            assertThat(back.getVersion()).isEqualTo(3L);
        }

        @Test
        void shouldMapFundToEntityAndBack() {
            var fund = Fund.builder()
                    .id(1L).name("FPV_RECAUDADORA")
                    .minimumAmount(new BigDecimal("75000"))
                    .category(FundCategoryEnum.FPV)
                    .build();

            var data = DynamoDbMapper.toData(fund);
            assertThat(data.getCategory()).isEqualTo("FPV");

            var back = DynamoDbMapper.toEntity(data);
            assertThat(back.getId()).isEqualTo(1L);
            assertThat(back.getCategory()).isEqualTo(FundCategoryEnum.FPV);
        }

        @Test
        void shouldMapTransactionToEntityAndBack() {
            var tx = Transaction.builder()
                    .id("tx-1").clientId("CLI-001").fundId(1L).fundName("Fund A")
                    .type(TransactionTypeEnum.SUBSCRIBE).amount(new BigDecimal("75000"))
                    .createdAt(LocalDateTime.of(2026, 3, 16, 10, 0))
                    .build();

            var data = DynamoDbMapper.toData(tx);
            assertThat(data.getClientIdFundId()).isEqualTo("CLI-001#1");
            assertThat(data.getType()).isEqualTo("SUBSCRIBE");
            assertThat(data.getCreatedAt()).isEqualTo("2026-03-16T10:00");

            var back = DynamoDbMapper.toEntity(data);
            assertThat(back.getId()).isEqualTo("tx-1");
            assertThat(back.getType()).isEqualTo(TransactionTypeEnum.SUBSCRIBE);
            assertThat(back.getCreatedAt()).isEqualTo(LocalDateTime.of(2026, 3, 16, 10, 0));
        }
    }

    @Nested
    class ClientAdapter {

        @Test
        void shouldFindClientById() {
            var entity = ClientEntity.builder()
                    .id("CLI-001").name("Andres").email("a@test.com").phone("300")
                    .balance(new BigDecimal("500000")).notificationPreference("EMAIL")
                    .build();

            when(clientTable.getItem(any(Key.class))).thenReturn(entity);

            StepVerifier.create(clientAdapter.findById("CLI-001"))
                    .assertNext(c -> {
                        assertThat(c.getId()).isEqualTo("CLI-001");
                        assertThat(c.getName()).isEqualTo("Andres");
                    })
                    .verifyComplete();
        }

        @Test
        void shouldReturnEmptyWhenClientNotFound() {
            when(clientTable.getItem(any(Key.class))).thenReturn(null);

            StepVerifier.create(clientAdapter.findById("CLI-999"))
                    .verifyComplete();
        }

        @Test
        void shouldSaveClientWithVersionIncrement() {
            var client = Client.builder()
                    .id("CLI-001").name("Andres").email("a@test.com").phone("300")
                    .balance(new BigDecimal("500000"))
                    .notificationPreference(NotificationPreferenceEnum.EMAIL)
                    .version(1L)
                    .build();

            StepVerifier.create(clientAdapter.save(client))
                    .assertNext(c -> {
                        assertThat(c.getId()).isEqualTo("CLI-001");
                        assertThat(c.getVersion()).isEqualTo(2L);
                    })
                    .verifyComplete();

            verify(clientTable).putItem(any(PutItemEnhancedRequest.class));
        }

        @Test
        void shouldThrowOptimisticLockExceptionOnConflict() {
            var client = Client.builder()
                    .id("CLI-001").name("Andres").email("a@test.com").phone("300")
                    .balance(new BigDecimal("500000"))
                    .notificationPreference(NotificationPreferenceEnum.EMAIL)
                    .version(1L)
                    .build();

            doThrow(ConditionalCheckFailedException.builder().message("Condition not met").build())
                    .when(clientTable).putItem(any(PutItemEnhancedRequest.class));

            StepVerifier.create(clientAdapter.save(client))
                    .expectError(OptimisticLockException.class)
                    .verify();
        }

        @Test
        void shouldMapDynamoExceptionToPersistenceError() {
            when(clientTable.getItem(any(Key.class))).thenThrow(new RuntimeException("DynamoDB failure"));

            StepVerifier.create(clientAdapter.findById("CLI-001"))
                    .expectErrorMatches(e -> e instanceof BusinessException
                            && ((BusinessException) e).getOpcode().equals("48"))
                    .verify();
        }
    }

    @Nested
    class FundAdapter {

        @Test
        void shouldFindFundById() {
            var entity = FundEntity.builder()
                    .id(1L).name("FPV_RECAUDADORA")
                    .minimumAmount(new BigDecimal("75000")).category("FPV")
                    .build();

            when(fundTable.getItem(any(Key.class))).thenReturn(entity);

            StepVerifier.create(fundAdapter.findById(1L))
                    .assertNext(f -> {
                        assertThat(f.getId()).isEqualTo(1L);
                        assertThat(f.getName()).isEqualTo("FPV_RECAUDADORA");
                    })
                    .verifyComplete();
        }


        @Test
        void shouldMapDynamoExceptionToPersistenceError() {
            when(fundTable.getItem(any(Key.class))).thenThrow(new RuntimeException("DynamoDB failure"));

            StepVerifier.create(fundAdapter.findById(1L))
                    .expectErrorMatches(e -> e instanceof BusinessException
                            && ((BusinessException) e).getOpcode().equals("48"))
                    .verify();
        }
    }

    @Nested
    class TransactionAdapter {

        @Test
        void shouldSaveTransaction() {
            var tx = Transaction.builder()
                    .id("tx-1").clientId("CLI-001").fundId(1L).fundName("Fund A")
                    .type(TransactionTypeEnum.SUBSCRIBE).amount(new BigDecimal("75000"))
                    .createdAt(LocalDateTime.now())
                    .build();

            StepVerifier.create(transactionAdapter.save(tx))
                    .assertNext(saved -> assertThat(saved.getId()).isEqualTo("tx-1"))
                    .verifyComplete();

            verify(transactionTable).putItem(any(TransactionEntity.class));
        }

        @Test
        void shouldFindByClientId() {
            var entity = TransactionEntity.builder()
                    .id("tx-1").clientId("CLI-001").fundId(1L).fundName("Fund A")
                    .type("SUBSCRIBE").amount(new BigDecimal("75000"))
                    .createdAt("2026-03-16T10:00").clientIdFundId("CLI-001#1")
                    .build();

            var page = Page.builder(TransactionEntity.class).items(List.of(entity)).build();
            var pageIterable = mock(software.amazon.awssdk.enhanced.dynamodb.model.PageIterable.class);
            when(clientIdIndex.query(any(QueryConditional.class))).thenReturn(pageIterable);
            when(pageIterable.stream()).thenReturn(Stream.of(page));

            StepVerifier.create(transactionAdapter.findByClientId("CLI-001"))
                    .assertNext(tx -> {
                        assertThat(tx.getId()).isEqualTo("tx-1");
                        assertThat(tx.getType()).isEqualTo(TransactionTypeEnum.SUBSCRIBE);
                    })
                    .verifyComplete();
        }

        @Test
        void shouldFindLastByClientIdAndFundId() {
            var older = TransactionEntity.builder()
                    .id("tx-1").clientId("CLI-001").fundId(1L).fundName("Fund A")
                    .type("SUBSCRIBE").amount(new BigDecimal("75000"))
                    .createdAt("2026-03-16T10:00").clientIdFundId("CLI-001#1")
                    .build();
            var newer = TransactionEntity.builder()
                    .id("tx-2").clientId("CLI-001").fundId(1L).fundName("Fund A")
                    .type("CANCEL").amount(new BigDecimal("75000"))
                    .createdAt("2026-03-16T12:00").clientIdFundId("CLI-001#1")
                    .build();

            var page = Page.builder(TransactionEntity.class).items(List.of(older, newer)).build();
            var pageIterable = mock(software.amazon.awssdk.enhanced.dynamodb.model.PageIterable.class);
            when(clientIdFundIdIndex.query(any(QueryConditional.class))).thenReturn(pageIterable);
            when(pageIterable.stream()).thenReturn(Stream.of(page));

            StepVerifier.create(transactionAdapter.findLastByClientIdAndFundId("CLI-001", 1L))
                    .assertNext(tx -> {
                        assertThat(tx.getId()).isEqualTo("tx-2");
                        assertThat(tx.getType()).isEqualTo(TransactionTypeEnum.CANCEL);
                    })
                    .verifyComplete();
        }

        @Test
        void shouldReturnEmptyWhenNoTransactionsForClientAndFund() {
            var page = Page.builder(TransactionEntity.class).items(List.<TransactionEntity>of()).build();
            var pageIterable = mock(software.amazon.awssdk.enhanced.dynamodb.model.PageIterable.class);
            when(clientIdFundIdIndex.query(any(QueryConditional.class))).thenReturn(pageIterable);
            when(pageIterable.stream()).thenReturn(Stream.of(page));

            StepVerifier.create(transactionAdapter.findLastByClientIdAndFundId("CLI-001", 99L))
                    .verifyComplete();
        }

        @Test
        void shouldMapDynamoExceptionToPersistenceError() {
            doThrow(new RuntimeException("DynamoDB failure")).when(transactionTable).putItem(any(TransactionEntity.class));

            var tx = Transaction.builder()
                    .id("tx-1").clientId("CLI-001").fundId(1L).fundName("Fund A")
                    .type(TransactionTypeEnum.SUBSCRIBE).amount(new BigDecimal("75000"))
                    .createdAt(LocalDateTime.now())
                    .build();

            StepVerifier.create(transactionAdapter.save(tx))
                    .expectErrorMatches(e -> e instanceof BusinessException
                            && ((BusinessException) e).getOpcode().equals("48"))
                    .verify();
        }
    }
}
