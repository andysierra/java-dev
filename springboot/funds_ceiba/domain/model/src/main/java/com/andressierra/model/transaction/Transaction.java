package com.andressierra.model.transaction;

import com.andressierra.model.transaction.enums.TransactionTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    private String id;
    private String clientId;
    private Long fundId;
    private String fundName;
    private TransactionTypeEnum type;
    private BigDecimal amount;
    private LocalDateTime createdAt;
}