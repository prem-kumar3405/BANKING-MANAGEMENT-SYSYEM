package com.prem.banking_management_system.accounts.transactions;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IdempotencyKeyRepository extends JpaRepository<IdempotencyKey,Long> {

    Optional<IdempotencyKey> findByIdempotencyKey(String idempotencyKey);
}
