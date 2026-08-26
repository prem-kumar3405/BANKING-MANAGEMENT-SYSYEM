package com.prem.banking_management_system;

import com.prem.banking_management_system.accounts.AccountRepository;
import com.prem.banking_management_system.accounts.transactions.TransactionService;
import com.prem.banking_management_system.accounts.transactions.WithdrawalRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
class ConcurrencyTest {


    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AccountRepository accountRepository;

    Long accountId=1L;

    @Test
    void testTwoThreads() {

        ExecutorService executor =
                Executors.newFixedThreadPool(2);

        executor.submit(() -> {
            try {
                transactionService.withdrawal(
                        new WithdrawalRequest(
                                accountId,
                                new BigDecimal("800.00")
                        )
                );

                System.out.println("Thread 1 → SUCCESS");

            } catch (Exception e) {
                System.out.println(
                        "Thread 1 → FAILED: "
                                + e.getClass().getSimpleName()
                                + " : "
                                + e.getMessage()
                );
            }
        });

        executor.submit(() -> {
            try {
                transactionService.withdrawal(
                        new WithdrawalRequest(
                                accountId,
                                new BigDecimal("800.00")
                        )
                );

                System.out.println("Thread 2 → SUCCESS");

            } catch (Exception e) {
                System.out.println(
                        "Thread 2 → FAILED: "
                                + e.getClass().getSimpleName()
                                + " : "
                                + e.getMessage()
                );
            }
        });


        executor.shutdown();

        while (!executor.isTerminated()) {
        }
    }
}
