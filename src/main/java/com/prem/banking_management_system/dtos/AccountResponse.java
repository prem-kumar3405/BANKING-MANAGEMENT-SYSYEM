package com.prem.banking_management_system.dtos;
import com.prem.banking_management_system.accounts.AccountStatus;
import com.prem.banking_management_system.accounts.AccountType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@AllArgsConstructor
@Getter
public class AccountResponse {

    Long id;
    String accountNumber;
    Long customerId;
    AccountType accountType;
    BigDecimal balance;
    AccountStatus status;
    LocalDateTime createAt;
}
