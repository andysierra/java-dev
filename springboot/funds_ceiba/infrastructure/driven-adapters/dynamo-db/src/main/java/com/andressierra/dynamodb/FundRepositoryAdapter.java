package com.andressierra.dynamodb;

import com.andressierra.dynamodb.config.DynamoDbProperties;
import com.andressierra.dynamodb.entity.FundEntity;
import com.andressierra.dynamodb.helper.DynamoDbMapper;
import com.andressierra.model.exception.BusinessException;
import com.andressierra.model.fund.Fund;
import com.andressierra.model.fund.gateways.FundRepository;
import com.andressierra.model.messages.MessagesEnum;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Repository
public class FundRepositoryAdapter implements FundRepository {

    private final DynamoDbTable<FundEntity> table;

    public FundRepositoryAdapter(DynamoDbEnhancedClient enhancedClient, DynamoDbProperties properties) {
        this.table = enhancedClient.table(properties.fundsTable(), TableSchema.fromBean(FundEntity.class));
    }

    @Override
    public Mono<Fund> findById(Long id) {
        return Mono.fromCallable(() -> table.getItem(Key.builder().partitionValue(id).build()))
                .map(DynamoDbMapper::toEntity)
                .onErrorMap(e -> !(e instanceof BusinessException),
                        e -> BusinessException.fromMessage(MessagesEnum.PERSISTENCE_ERROR));
    }

    @Override
    public Flux<Fund> findAll() {
        return Flux.fromIterable(table.scan().items())
                .map(DynamoDbMapper::toEntity)
                .onErrorMap(e -> !(e instanceof BusinessException),
                        e -> BusinessException.fromMessage(MessagesEnum.PERSISTENCE_ERROR));
    }
}
