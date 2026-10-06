package com.andressierra.dynamodb;

import com.andressierra.dynamodb.config.DynamoDbProperties;
import com.andressierra.dynamodb.entity.ClientEntity;
import com.andressierra.dynamodb.helper.DynamoDbMapper;
import com.andressierra.model.client.Client;
import com.andressierra.model.client.gateways.ClientRepository;
import com.andressierra.model.exception.BusinessException;
import com.andressierra.model.exception.OptimisticLockException;
import com.andressierra.model.messages.MessagesEnum;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.ConditionalCheckFailedException;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.Map;

@Repository
public class ClientRepositoryAdapter implements ClientRepository {

    private final DynamoDbTable<ClientEntity> table;

    public ClientRepositoryAdapter(DynamoDbEnhancedClient enhancedClient, DynamoDbProperties properties) {
        this.table = enhancedClient.table(properties.clientsTable(), TableSchema.fromBean(ClientEntity.class));
    }

    @Override
    public Mono<Client> findById(String id) {
        return Mono.fromCallable(() -> table.getItem(Key.builder().partitionValue(id).build()))
                .map(DynamoDbMapper::toEntity)
                .onErrorMap(e -> !(e instanceof BusinessException),
                        e -> BusinessException.fromMessage(MessagesEnum.PERSISTENCE_ERROR));
    }

    @Override
    public Mono<Client> save(Client client) {
        return Mono.fromCallable(() -> {
                    var entity = DynamoDbMapper.toData(client);
                    long currentVersion = client.getVersion() != null ? client.getVersion() : 0L;
                    long newVersion = currentVersion + 1;
                    entity.setVersion(newVersion);

                    Expression conditionExpression = buildVersionCondition(currentVersion);

                    var request = PutItemEnhancedRequest.builder(ClientEntity.class)
                            .item(entity)
                            .conditionExpression(conditionExpression)
                            .build();

                    table.putItem(request);
                    client.setVersion(newVersion);
                    return client;
                })
                .onErrorMap(ConditionalCheckFailedException.class, e -> new OptimisticLockException())
                .onErrorMap(e -> !(e instanceof BusinessException) && !(e instanceof OptimisticLockException),
                        e -> BusinessException.fromMessage(MessagesEnum.PERSISTENCE_ERROR));
    }

    private Expression buildVersionCondition(long currentVersion) {
        if (currentVersion == 0L) {
            return Expression.builder()
                    .expression("attribute_not_exists(version) OR version = :ver")
                    .expressionValues(Map.of(":ver", AttributeValue.builder().n(String.valueOf(currentVersion)).build()))
                    .build();
        }
        return Expression.builder()
                .expression("version = :ver")
                .expressionValues(Map.of(":ver", AttributeValue.builder().n(String.valueOf(currentVersion)).build()))
                .build();
    }
}
