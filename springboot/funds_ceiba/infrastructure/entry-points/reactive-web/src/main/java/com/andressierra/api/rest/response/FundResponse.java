package com.andressierra.api.rest.response;

import com.andressierra.model.fund.enums.FundCategoryEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FundResponse {
    private Long id;
    private String name;
    private BigDecimal minimumAmount;
    private FundCategoryEnum category;
}