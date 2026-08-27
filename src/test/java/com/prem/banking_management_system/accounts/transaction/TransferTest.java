package com.prem.banking_management_system.accounts.transaction;

import com.prem.banking_management_system.accounts.Account;
import com.prem.banking_management_system.accounts.AccountRepository;
import com.prem.banking_management_system.accounts.transactions.TransactionStatus;
import com.prem.banking_management_system.accounts.transactions.TransferRequest;
import com.prem.banking_management_system.accounts.transactions.TransferResponse;
import com.prem.banking_management_system.accounts.transactions.TransactionService;
import com.prem.banking_management_system.exceptions.AccountNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TransferTest {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void testSuccessfulTransfer() {

        Long fromAccountId = 1L;
        Long toAccountId = 2L;

        Account fromAccountBefore =
                accountRepository.findById(fromAccountId)
                        .orElseThrow();

        Account toAccountBefore =
                accountRepository.findById(toAccountId)
                        .orElseThrow();

        BigDecimal amount = new BigDecimal("1000.00");

        BigDecimal fromBalanceBefore =
                fromAccountBefore.getBalance();

        BigDecimal toBalanceBefore =
                toAccountBefore.getBalance();

        TransferResponse response =
                transactionService.transfer(
                        new TransferRequest(
                                fromAccountId,
                                toAccountId,
                                amount
                        )
                );

        Account fromAccountAfter =
                accountRepository.findById(fromAccountId)
                        .orElseThrow();

        Account toAccountAfter =
                accountRepository.findById(toAccountId)
                        .orElseThrow();

        assertEquals(
                TransactionStatus.SUCCESS,
                response.status()
        );

        assertEquals(
                fromBalanceBefore.subtract(amount),
                fromAccountAfter.getBalance()
        );

        assertEquals(
                toBalanceBefore.add(amount),
                toAccountAfter.getBalance()
        );
    }
    @Test
    void testTransferRollback() {

        Long fromAccountId = 1L;
        Long invalidToAccountId = 9999L;

        Account fromAccountBefore =
                accountRepository.findById(fromAccountId)
                        .orElseThrow();

        BigDecimal balanceBefore =
                fromAccountBefore.getBalance();

        TransferRequest request =
                new TransferRequest(
                        fromAccountId,
                        invalidToAccountId,
                        new BigDecimal("1000.00")
                );

        assertThrows(
                AccountNotFoundException.class,
                () -> transactionService.transfer(request)
        );

        Account fromAccountAfter =
                accountRepository.findById(fromAccountId)
                        .orElseThrow();

        assertEquals(
                balanceBefore,
                fromAccountAfter.getBalance()
        );
    }

}
