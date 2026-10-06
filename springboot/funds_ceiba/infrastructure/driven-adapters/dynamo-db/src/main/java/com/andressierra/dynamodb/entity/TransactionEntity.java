package com.andressierra.dynamodb.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;

import java.math.BigDecimal;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@DynamoDbBean
public class TransactionEntity {
    private String id;
    private String clientId;
    private Long fundId;
    private String fundName;
    private String type;
    private BigDecimal amount;
    private String createdAt;
    private String clientIdFundId;

    @DynamoDbPartitionKey
    public String getId() {
        return id;
    }

    @DynamoDbSecondaryPartitionKey(indexNames = "clientId-index")
    public String getClientId() {
        return clientId;
    }

    @DynamoDbSecondaryPartitionKey(indexNames = "clientIdFundId-index")
    public String getClientIdFundId() {
        return clientIdFundId;
    }
}
