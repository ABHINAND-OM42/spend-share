package com.spendshare.spendshare.settlement.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SettlementDTO {

    @NotNull(message = "Payer ID is required")
    private Long payerId;

    @NotNull(message = "Payee ID is required")
    private Long payeeId;

    @NotNull(message = "Group ID is required")
    private Long groupId;

    @NotNull(message = "Amount is required")
    @Min(value = 1, message = "Amount must be positive")
    private BigDecimal amount;
}
