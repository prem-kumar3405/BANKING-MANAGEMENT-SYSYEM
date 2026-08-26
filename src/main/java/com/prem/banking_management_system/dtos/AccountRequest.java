package com.prem.banking_management_system.dtos;

import com.prem.banking_management_system.accounts.AccountType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record AccountRequest(

        @NotNull
        Long customerId,

        @NotNull
        AccountType accountType,

        @NotNull
        @DecimalMin(value = "0.00")
        BigDecimal initialBalance

)
{
}