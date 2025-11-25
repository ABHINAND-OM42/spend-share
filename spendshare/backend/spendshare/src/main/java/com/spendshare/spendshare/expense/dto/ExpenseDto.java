package com.spendshare.spendshare.expense.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ExpenseDto {

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Total amount is required")
    @Min(value = 1, message = "Amount must be greater than 0")
    private BigDecimal totalAmount;

    @NotNull(message = "Payer ID is required")
    private Long paidByUserId;

    @NotNull(message = "Group ID is required")
    private Long groupId;

    @Pattern(regexp = "EQUAL|EXACT", message = "Split type must be EQUAL or EXSACT")
    private String splitTYpe;

    @NotNull(message = "Split details are required")
    private List<SplitDefinition> splits;

    @Data
    public static class SplitDefinition{
        @NotNull(message = "User ID is required")
        private long userId;

        private BigDecimal amount;
    }
}
