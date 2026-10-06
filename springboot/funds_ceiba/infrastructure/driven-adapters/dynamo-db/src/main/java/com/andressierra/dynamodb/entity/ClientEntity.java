package com.andressierra.dynamodb.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.math.BigDecimal;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@DynamoDbBean
public class ClientEntity {
    private String id;
    private String name;
    private String email;
    private String phone;
    private BigDecimal balance;
    private String notificationPreference;
    private Long version;

    @DynamoDbPartitionKey
    public String getId() {
        return id;
    }
}
