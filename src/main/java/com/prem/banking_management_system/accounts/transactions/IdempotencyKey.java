package com.prem.banking_management_system.accounts.transactions;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "idempotency_keys",
uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_idempotency_key",
                columnNames = "idempotency_key"
        )
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IdempotencyKey {

    @Id
    @GeneratedValue(strategy  =GenerationType.IDENTITY)
    private Long id;

    @Column(name = "idempotency_key",
    nullable = false,
    unique = true)
    private String idempotencyKey;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
