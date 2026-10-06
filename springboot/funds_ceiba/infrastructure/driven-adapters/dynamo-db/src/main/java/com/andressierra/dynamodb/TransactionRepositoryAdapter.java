package com.andressierra.dynamodb;

import com.andressierra.dynamodb.config.DynamoDbProperties;
import com.andressierra.dynamodb.entity.TransactionEntity;
import com.andressierra.dynamodb.helper.DynamoDbMapper;
import com.andressierra.model.exception.BusinessException;
import com.andressierra.model.messages.MessagesEnum;
import com.andressierra.model.transaction.Transaction;
import com.andressierra.model.transaction.gateways.TransactionRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbIndex;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

@Repository
public class TransactionRepositoryAdapter implements TransactionRepository {

    private final DynamoDbTable<TransactionEntity> table;
    private final DynamoDbIndex<TransactionEntity> clientIdIndex;
    private final DynamoDbIndex<TransactionEntity> clientIdFundIdIndex;

    public TransactionRepositoryAdapter(DynamoDbEnhancedClient enhancedClient, DynamoDbProperties properties) {
        this.table = enhancedClient.table(properties.transactionsTable(), TableSchema.fromBean(TransactionEntity.class));
        this.clientIdIndex = table.index("clientId-index");
        this.clientIdFundIdIndex = table.index("clientIdFundId-index");
    }

    @Override
    public Mono<Transaction> save(Transaction transaction) {
        return Mono.fromCallable(() -> {
                    table.putItem(DynamoDbMapper.toData(transaction));
                    return transaction;
                })
                .onErrorMap(e -> !(e instanceof BusinessException),
                        e -> BusinessException.fromMessage(MessagesEnum.PERSISTENCE_ERROR));
    }

    @Override
    public Flux<Transaction> findByClientId(String clientId) {
        var queryConditional = QueryConditional.keyEqualTo(Key.builder().partitionValue(clientId).build());
        return Flux.fromIterable(
                        clientIdIndex.query(queryConditional).stream()
                                .flatMap(page -> page.items().stream()).toList()
                )
                .map(DynamoDbMapper::toEntity)
                .onErrorMap(e -> !(e instanceof BusinessException),
                        e -> BusinessException.fromMessage(MessagesEnum.PERSISTENCE_ERROR));
    }

    @Override
    public Mono<Transaction> findLastByClientIdAndFundId(String clientId, Long fundId) {
        var compositeKey = clientId + "#" + fundId;
        var queryConditional = QueryConditional.keyEqualTo(Key.builder().partitionValue(compositeKey).build());
        return Flux.fromIterable(
                        clientIdFundIdIndex.query(queryConditional).stream()
                                .flatMap(page -> page.items().stream()).toList()
                )
                .map(DynamoDbMapper::toEntity)
                .sort((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .next()
                .onErrorMap(e -> !(e instanceof BusinessException),
                        e -> BusinessException.fromMessage(MessagesEnum.PERSISTENCE_ERROR));
    }
}
