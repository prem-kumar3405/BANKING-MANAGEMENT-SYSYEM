package com.prem.banking_management_system.dtos;

import java.time.LocalDateTime;

public record CustomerResponse(Long id,
                               String name,
                               String email,
                               String phone,
                               LocalDateTime createdAt) {
}
