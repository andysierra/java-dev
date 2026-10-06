package com.andressierra.model.fund;

import com.andressierra.model.fund.enums.FundCategoryEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Fund {
    private Long id;
    private String name;
    private BigDecimal minimumAmount;
    private FundCategoryEnum category;
}