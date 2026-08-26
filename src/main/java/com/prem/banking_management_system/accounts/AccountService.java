package com.prem.banking_management_system.accounts;


import com.prem.banking_management_system.customers.Customer;
import com.prem.banking_management_system.customers.CustomerRepository;
import com.prem.banking_management_system.dtos.AccountRequest;
import com.prem.banking_management_system.dtos.AccountResponse;
import com.prem.banking_management_system.exceptions.AccountNotFoundException;
import com.prem.banking_management_system.exceptions.CustomerNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

  private final AccountRepository accountRepository;
  private final CustomerRepository customerRepository;


  public AccountResponse createAccount(AccountRequest request){

      Customer customer = customerRepository.findById(request.customerId())
              .orElseThrow(()->new CustomerNotFoundException("Customer Not found"));

      // creating an account
      Account account = new Account();
      account.setCustomer(customer);

      // generate the account number
      account.setAccountNumber(generateAccountNumber());

      account.setAccountType(request.accountType());
      account.setBalance(request.initialBalance());
      account.setStatus(AccountStatus.ACTIVE);

      account.setCreatedAt(LocalDateTime.now());

      Account saved = accountRepository.save(account);



      return new AccountResponse(
              saved.getId(),
              saved.getAccountNumber(),
              saved.getCustomer().getId(),
              saved.getAccountType(),
              saved.getBalance(),
              saved.getStatus(),
              saved.getCreatedAt()
      );



  }
  public AccountResponse getAccountByNumber(String accountNumber){

      Account account = accountRepository.findByAccountNumber(accountNumber)
              .orElseThrow(()->(new AccountNotFoundException("Account Not Found")));

      return new AccountResponse(
              account.getId(),
              account.getAccountNumber(),
              account.getCustomer().getId(),
              account.getAccountType(),
              account.getBalance(),
              account.getStatus(),
              account.getCreatedAt()

      );

  }


  public List<AccountResponse> getAllAccounts(){

      //Long id;
      //    String accountNumber;
      //    Long customerId;
      //    AccountType accountType;
      //    BigDecimal balance;
      //    AccountStatus status;
      //    LocalDateTime createAt;
      return accountRepository.findAll()
              .stream()
              .map(account -> new AccountResponse(
                      account.getId(),
                      account.getAccountNumber(),
                      account.getCustomer().getId(),
                      account.getAccountType(),
                      account.getBalance(),
                      account.getStatus(),
                      account.getCreatedAt()
              )).toList();
  }
  private String generateAccountNumber()
  {
       return "ACC"+ UUID.randomUUID()
               .toString()
               .replace("-","")
               .substring(0,12)
               .toUpperCase();


  }

}
