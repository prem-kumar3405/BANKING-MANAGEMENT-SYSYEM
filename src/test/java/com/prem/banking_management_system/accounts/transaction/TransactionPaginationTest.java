package com.prem.banking_management_system.accounts.transaction;

import com.prem.banking_management_system.accounts.transactions.TransactionResponse;
import com.prem.banking_management_system.accounts.transactions.TransactionService;
import com.prem.banking_management_system.accounts.transactions.TransactionType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TransactionPaginationTest {

    @Autowired
    private TransactionService transactionService;

    @Test
    void testTransactionPaginationAndSorting() {

        PageRequest pageable = PageRequest.of(
                0,
                5,
                Sort.by(Sort.Direction.DESC, "createAt")
        );

        Page<TransactionResponse> result =
                transactionService.getTransactionsByAccountId(
                        1L,
                        TransactionType.DEPOSIT,
                        pageable
                );

        assertEquals(5, result.getSize());

        assertEquals(0, result.getNumber());

        assertTrue(result.getTotalElements() > 0);

        assertTrue(result.getTotalPages() > 0);

        assertTrue(result.getContent().size() <= 5);

        for (int i = 0; i < result.getContent().size() - 1; i++) {

            assertFalse(
                    result.getContent()
                            .get(i)
                            .createdAt()
                            .isBefore(
                                    result.getContent()
                                            .get(i + 1)
                                            .createdAt()
                            )
            );
        }
    }
}