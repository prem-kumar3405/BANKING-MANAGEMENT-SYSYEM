package com.prem.banking_management_system.accounts.transactions;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponse(

        Long id,
        Long accountId,
        TransactionType transactionType,
        BigDecimal amount,
        TransactionStatus status,
        LocalDateTime createdAt

) {
}
