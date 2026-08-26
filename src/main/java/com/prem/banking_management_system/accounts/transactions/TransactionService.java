package com.prem.banking_management_system.accounts.transactions;

import com.prem.banking_management_system.accounts.Account;
import com.prem.banking_management_system.accounts.AccountRepository;
import com.prem.banking_management_system.accounts.AccountStatus;
import com.prem.banking_management_system.exceptions.AccountNotActiveException;
import com.prem.banking_management_system.exceptions.AccountNotFoundException;
import com.prem.banking_management_system.exceptions.InsufficientBalanceException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Transactional
    public TransactionResponse deposit(DepositRequest request){

        Account account = accountRepository
                .findById(request.accountId())
                .orElseThrow(()-> new AccountNotFoundException(
                        "Account not found"
                ));

        BigDecimal newBalance = account.getBalance().add(request.amount());

        account.setBalance(newBalance);


        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setAmount(request.amount());
        transaction.setTransactionStatus(TransactionStatus.SUCCESS);
        transaction.setCreateAt(LocalDateTime.now());

        Transaction saved = transactionRepository.save(transaction);


        return new TransactionResponse(
                saved.getId(),
                saved.getAccount().getId(),
                saved.getTransactionType(),
                saved.getAmount(),
                saved.getTransactionStatus(),
                saved.getCreateAt()


        );


    }

    @Transactional
    public TransactionResponse withdrawal(WithdrawalRequest request){

        Account account = accountRepository.findById(request.accountId())
                .orElseThrow(()->(new AccountNotFoundException("Account Not  found")));

        if(account.getStatus()!= AccountStatus.ACTIVE)
        {
            throw new AccountNotActiveException("Account is status is not active");
        }

        if(account.getBalance().compareTo(request.amount())<0){
            throw new InsufficientBalanceException("Insufficient balance");

        }
        BigDecimal newBalance =
                account.getBalance().subtract(request.amount());

        account.setBalance(newBalance);

        Transaction transaction = new Transaction();

        transaction.setAccount(account);
        transaction.setTransactionType(TransactionType.WITHDRAWAL);
        transaction.setAmount(request.amount());
        transaction.setTransactionStatus(TransactionStatus.SUCCESS);
        transaction.setCreateAt(LocalDateTime.now());

        Transaction saved =
                transactionRepository.save(transaction);
        return new TransactionResponse(
                saved.getId(),
                saved.getAccount().getId(),
                saved.getTransactionType(),
                saved.getAmount(),
                saved.getTransactionStatus(),
                saved.getCreateAt()
        );
    }

    public List<TransactionResponse> getTransactionsByAccountId(Long accountId) {

        // Check whether account exists
        accountRepository.findById(accountId)
                .orElseThrow(() ->
                        new AccountNotFoundException(
                                "Account not found"
                        ));

        return transactionRepository
                .findByAccountId(accountId)
                .stream()
                .map(transaction -> new TransactionResponse(
                        transaction.getId(),
                        transaction.getAccount().getId(),
                        transaction.getTransactionType(),
                        transaction.getAmount(),
                        transaction.getTransactionStatus(),
                        transaction.getCreateAt()
                ))
                .toList();
    }
}
