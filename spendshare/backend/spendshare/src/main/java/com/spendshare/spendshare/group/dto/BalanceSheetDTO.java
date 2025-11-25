package com.spendshare.spendshare.group.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class BalanceSheetDTO {

    private String owedBy;
    private String owedTo;
    private BigDecimal amount;

}
