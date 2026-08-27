package com.prem.banking_management_system;

import com.prem.banking_management_system.accounts.Account;
import com.prem.banking_management_system.accounts.AccountRepository;
import com.prem.banking_management_system.accounts.transactions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class IdempotencyTest {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void sameIdempotencyKeyShouldNotTransferTwice() {

        Long fromAccountId = 1L;
        Long toAccountId = 2L;

        BigDecimal amount = new BigDecimal("1000.00");

        Account fromBefore =
                accountRepository.findById(fromAccountId)
                        .orElseThrow();

        Account toBefore =
                accountRepository.findById(toAccountId)
                        .orElseThrow();

        BigDecimal fromBalanceBefore =
                fromBefore.getBalance();

        BigDecimal toBalanceBefore =
                toBefore.getBalance();

        String idempotencyKey = "test-key-123";

        // First request
        transactionService.transfer(
                idempotencyKey,
                new TransferRequest(
                        fromAccountId,
                        toAccountId,
                        amount
                )
        );

        // Second request with SAME key
        transactionService.transfer(
                idempotencyKey,
                new TransferRequest(
                        fromAccountId,
                        toAccountId,
                        amount
                )
        );

        Account fromAfter =
                accountRepository.findById(fromAccountId)
                        .orElseThrow();

        Account toAfter =
                accountRepository.findById(toAccountId)
                        .orElseThrow();

        // Only one transfer should happen
        assertEquals(
                fromBalanceBefore.subtract(amount),
                fromAfter.getBalance()
        );

        assertEquals(
                toBalanceBefore.add(amount),
                toAfter.getBalance()
        );
    }
}