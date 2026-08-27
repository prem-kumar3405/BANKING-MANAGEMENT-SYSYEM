package com.prem.banking_management_system.accounts.transactions;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    Page<Transaction> findByAccountId(Long accountId, Pageable pageable);
    Page<Transaction> findByAccountIdAndTransactionType(
            Long accountId,
            TransactionType transactionType,
            Pageable pageable
    );

}
