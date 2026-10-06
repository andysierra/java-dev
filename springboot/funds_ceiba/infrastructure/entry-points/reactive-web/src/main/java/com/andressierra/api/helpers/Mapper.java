package com.andressierra.api.helpers;

import com.andressierra.api.rest.request.CancelSubscriptionRequest;
import com.andressierra.api.rest.request.SubscribeRequest;
import com.andressierra.api.rest.response.CancelSubscriptionResponse;
import com.andressierra.api.rest.response.FundResponse;
import com.andressierra.api.rest.response.SubscribeResponse;
import com.andressierra.api.rest.response.TransactionResponse;
import com.andressierra.model.fund.Fund;
import com.andressierra.model.transaction.Transaction;
import com.andressierra.usecase.cancel.CancelCommand;
import com.andressierra.usecase.subscribe.SubscribeCommand;

public class Mapper {
    private Mapper() {}

    public static SubscribeCommand toSubscribeCommand(SubscribeRequest request) {
        return SubscribeCommand.builder()
                .clientId(request.getClientId())
                .fundId(request.getFundId())
                .notificationPreference(request.getNotificationPreference())
                .build();
    }

    public static CancelCommand toCancelCommand(CancelSubscriptionRequest request) {
        return CancelCommand.builder()
                .clientId(request.getClientId())
                .fundId(request.getFundId())
                .build();
    }

    public static SubscribeResponse toSubscribeResponse(Transaction transaction) {
        return SubscribeResponse.builder()
                .transactionId(transaction.getId())
                .fundName(transaction.getFundName())
                .amount(transaction.getAmount())
                .build();
    }

    public static CancelSubscriptionResponse toCancelSubscriptionResponse(Transaction transaction) {
        return CancelSubscriptionResponse.builder()
                .transactionId(transaction.getId())
                .fundName(transaction.getFundName())
                .returnedAmount(transaction.getAmount())
                .build();
    }

    public static TransactionResponse toTransactionResponse(Transaction transaction) {
        return TransactionResponse.builder()
                .transactionId(transaction.getId())
                .fundId(transaction.getFundId())
                .fundName(transaction.getFundName())
                .type(transaction.getType())
                .amount(transaction.getAmount())
                .createdAt(transaction.getCreatedAt())
                .build();
    }

    public static FundResponse toFundResponse(Fund fund) {
        return FundResponse.builder()
                .id(fund.getId())
                .name(fund.getName())
                .minimumAmount(fund.getMinimumAmount())
                .category(fund.getCategory())
                .build();
    }
}
