package com.andressierra.api.rest.response;

import com.andressierra.model.transaction.enums.TransactionTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {
    private String transactionId;
    private Long fundId;
    private String fundName;
    private TransactionTypeEnum type;
    private BigDecimal amount;
    private LocalDateTime createdAt;
}