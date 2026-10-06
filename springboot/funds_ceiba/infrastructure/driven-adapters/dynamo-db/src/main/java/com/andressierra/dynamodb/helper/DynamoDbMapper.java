package com.andressierra.dynamodb.helper;

import com.andressierra.dynamodb.entity.ClientEntity;
import com.andressierra.dynamodb.entity.FundEntity;
import com.andressierra.dynamodb.entity.TransactionEntity;
import com.andressierra.model.client.Client;
import com.andressierra.model.client.enums.NotificationPreferenceEnum;
import com.andressierra.model.fund.Fund;
import com.andressierra.model.fund.enums.FundCategoryEnum;
import com.andressierra.model.transaction.Transaction;
import com.andressierra.model.transaction.enums.TransactionTypeEnum;

import java.time.LocalDateTime;

public class DynamoDbMapper {
    private DynamoDbMapper() {}

    public static ClientEntity toData(Client client) {
        return ClientEntity.builder()
                .id(client.getId())
                .name(client.getName())
                .email(client.getEmail())
                .phone(client.getPhone())
                .balance(client.getBalance())
                .notificationPreference(client.getNotificationPreference().name())
                .version(client.getVersion())
                .build();
    }

    public static Client toEntity(ClientEntity data) {
        return Client.builder()
                .id(data.getId())
                .name(data.getName())
                .email(data.getEmail())
                .phone(data.getPhone())
                .balance(data.getBalance())
                .notificationPreference(NotificationPreferenceEnum.valueOf(data.getNotificationPreference()))
                .version(data.getVersion())
                .build();
    }

    public static FundEntity toData(Fund fund) {
        return FundEntity.builder()
                .id(fund.getId())
                .name(fund.getName())
                .minimumAmount(fund.getMinimumAmount())
                .category(fund.getCategory().name())
                .build();
    }

    public static Fund toEntity(FundEntity data) {
        return Fund.builder()
                .id(data.getId())
                .name(data.getName())
                .minimumAmount(data.getMinimumAmount())
                .category(FundCategoryEnum.valueOf(data.getCategory()))
                .build();
    }

    public static TransactionEntity toData(Transaction transaction) {
        return TransactionEntity.builder()
                .id(transaction.getId())
                .clientId(transaction.getClientId())
                .fundId(transaction.getFundId())
                .fundName(transaction.getFundName())
                .type(transaction.getType().name())
                .amount(transaction.getAmount())
                .createdAt(transaction.getCreatedAt().toString())
                .clientIdFundId(transaction.getClientId() + "#" + transaction.getFundId())
                .build();
    }

    public static Transaction toEntity(TransactionEntity data) {
        return Transaction.builder()
                .id(data.getId())
                .clientId(data.getClientId())
                .fundId(data.getFundId())
                .fundName(data.getFundName())
                .type(TransactionTypeEnum.valueOf(data.getType()))
                .amount(data.getAmount())
                .createdAt(LocalDateTime.parse(data.getCreatedAt()))
                .build();
    }
}
