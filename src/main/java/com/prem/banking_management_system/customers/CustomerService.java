package com.prem.banking_management_system.customers;

import com.prem.banking_management_system.dtos.CustomerRequest;
import com.prem.banking_management_system.dtos.CustomerResponse;
import com.prem.banking_management_system.exceptions.CustomerAlreadyExistsExpection;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class CustomerService {



    private final CustomerRepository customerRepository;

    public CustomerResponse createCustomer(CustomerRequest request){

        if(customerRepository.existsByEmail(request.email())){
            throw new CustomerAlreadyExistsExpection("Email already registered");
        }

        Customer customer = new Customer();

        customer.setName(request.name());
        customer.setEmail(request.email());
        customer.setPhone(request.phone());
        customer.setCreateAt(LocalDateTime.now());

        Customer saved = customerRepository.save(customer);

        return new CustomerResponse(
                saved.getId(),
                saved.getName(),
                saved.getEmail(),
                saved.getPhone(),
                saved.getCreateAt()
        );
    }

    public List<CustomerResponse> getAllCustomers(){
        return customerRepository.findAll()
                .stream().map(customer -> new CustomerResponse(
                  customer.getId(),
                  customer.getName(),
                        customer.getEmail(),
                        customer.getPhone(),
                        customer.getCreateAt()
                )).toList();
    }

}

