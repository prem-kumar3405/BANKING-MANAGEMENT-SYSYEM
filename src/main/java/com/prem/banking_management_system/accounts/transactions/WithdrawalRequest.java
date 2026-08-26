package com.prem.banking_management_system.accounts.transactions;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record WithdrawalRequest(
        @NotNull
        Long accountId,

        @NotNull
        @DecimalMin(value = "0.01")
        BigDecimal amount
) {
}
