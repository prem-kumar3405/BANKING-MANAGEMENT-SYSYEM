package com.prem.banking_management_system.accounts.transactions;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


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
    @PostMapping("/transfer")
    public ResponseEntity<TransferResponse> transfer(
           @RequestHeader("idempotency-key") String idempotencyKey,
            @Valid @RequestBody TransferRequest request
    ) {
        TransferResponse response =
                transactionService.transfer(idempotencyKey,request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
    @GetMapping("/account/{accountId}")
    public ResponseEntity<Page<TransactionResponse>> getTransactionByAccountId(
            @PathVariable Long accountId,
            @RequestParam(required = false)
            TransactionType type,

            @PageableDefault(
                    size = 20,
                    sort = "createAt",
                    direction = Sort.Direction.DESC
            ) Pageable pageable)
    {
        Page<TransactionResponse> transactions = transactionService.getTransactionsByAccountId(accountId,type, pageable);

        return ResponseEntity.ok(transactions);
    }

}
