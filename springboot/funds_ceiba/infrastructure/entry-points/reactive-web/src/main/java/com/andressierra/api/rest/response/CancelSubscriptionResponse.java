package com.andressierra.api.rest.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CancelSubscriptionResponse {
    private String transactionId;
    private String fundName;
    private BigDecimal returnedAmount;
}