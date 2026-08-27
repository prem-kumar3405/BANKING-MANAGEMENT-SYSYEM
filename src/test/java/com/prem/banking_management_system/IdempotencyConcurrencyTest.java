package com.prem.banking_management_system;

import com.prem.banking_management_system.accounts.Account;
import com.prem.banking_management_system.accounts.AccountRepository;
import com.prem.banking_management_system.accounts.transactions.TransactionService;
import com.prem.banking_management_system.accounts.transactions.TransferRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class IdempotencyConcurrencyTest {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void sameIdempotencyKeyShouldNotTransferTwiceConcurrently()
            throws Exception {

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

        String idempotencyKey =
                "concurrent-test-" + System.currentTimeMillis();

        ExecutorService executor =
                Executors.newFixedThreadPool(2);

        CountDownLatch startLatch =
                new CountDownLatch(1);

        Callable<Void> transferTask = () -> {

            startLatch.await();

            transactionService.transfer(
                    idempotencyKey,
                    new TransferRequest(
                            fromAccountId,
                            toAccountId,
                            amount
                    )
            );

            return null;
        };

        Future<Void> thread1 =
                executor.submit(transferTask);

        Future<Void> thread2 =
                executor.submit(transferTask);

        // Release both threads at approximately the same time
        startLatch.countDown();

        try {
            thread1.get();
        } catch (ExecutionException ignored) {
            // One concurrent request may fail because
            // the database unique constraint detects
            // the duplicate idempotency key.
        }

        try {
            thread2.get();
        } catch (ExecutionException ignored) {
            // Expected possibility for the losing request.
        }

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        Account fromAfter =
                accountRepository.findById(fromAccountId)
                        .orElseThrow();

        Account toAfter =
                accountRepository.findById(toAccountId)
                        .orElseThrow();

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