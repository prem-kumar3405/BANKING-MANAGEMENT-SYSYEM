package com.prem.banking_management_system.accounts.transactions;

import java.math.BigDecimal;

public record TransferResponse(
        Long fromAccountId,
        Long toAccountId,
        BigDecimal amount,
        TransactionStatus status
) {
}
