package com.andressierra.dynamodb.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "adapters.dynamodb")
public record DynamoDbProperties(
        String region,
        String clientsTable,
        String fundsTable,
        String transactionsTable) {
}
