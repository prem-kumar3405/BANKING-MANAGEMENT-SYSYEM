package com.prem.banking_management_system.accounts.transaction;

import com.prem.banking_management_system.accounts.transactions.TransactionResponse;
import com.prem.banking_management_system.accounts.transactions.TransactionService;
import com.prem.banking_management_system.accounts.transactions.TransactionType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TransactionFilterTest {

    @Autowired
    private TransactionService transactionService;

    @Test
    void shouldReturnOnlyDeposits() {

        PageRequest pageable = PageRequest.of(
                0,
                10,
                Sort.by(Sort.Direction.DESC, "createAt")
        );

        var result =
                transactionService.getTransactionsByAccountId(
                        1L,
                        TransactionType.DEPOSIT,
                        pageable
                );

        assertFalse(result.isEmpty());

        for (TransactionResponse transaction : result.getContent()) {
            assertEquals(
                    TransactionType.DEPOSIT,
                    transaction.transactionType()
            );
        }
    }

    @Test
    void shouldReturnOnlyWithdrawals() {

        PageRequest pageable = PageRequest.of(
                0,
                10,
                Sort.by(Sort.Direction.DESC, "createAt")
        );

        var result =
                transactionService.getTransactionsByAccountId(
                        1L,
                        TransactionType.WITHDRAWAL,
                        pageable
                );

        assertFalse(result.isEmpty());

        for (TransactionResponse transaction : result.getContent()) {
            assertEquals(
                    TransactionType.WITHDRAWAL,
                    transaction.transactionType()
            );
        }
    }
}