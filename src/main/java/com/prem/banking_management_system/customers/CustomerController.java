package com.prem.banking_management_system.customers;


import com.prem.banking_management_system.dtos.CustomerRequest;
import com.prem.banking_management_system.dtos.CustomerResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(@Valid @RequestBody CustomerRequest request){

      CustomerResponse response = customerService.createCustomer(request);


      return ResponseEntity.status(HttpStatus.CREATED)
              .body(response);

    }
    @GetMapping("/getAllCustomers")
    public ResponseEntity<List<CustomerResponse>> getAll(){

        List<CustomerResponse> customers = customerService.getAllCustomers();

        return ResponseEntity.ok(customers);
    }
}
