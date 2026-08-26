package com.prem.banking_management_system.accounts.transactions;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponse> deposit(@Valid @RequestBody DepositRequest request){
        TransactionResponse response = transactionService.deposit(request);


        return  ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponse> withdrawal(@Valid @RequestBody WithdrawalRequest request){
        TransactionResponse response = transactionService.withdrawal(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<TransactionResponse>> getTransactionByAccountId(@PathVariable Long accountId)
    {
        List<TransactionResponse> transactions = transactionService.getTransactionsByAccountId(accountId);

        return ResponseEntity.ok(transactions);
    }

}
