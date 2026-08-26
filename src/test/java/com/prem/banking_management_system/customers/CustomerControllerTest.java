package com.prem.banking_management_system.customers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.springframework.http.MediaType;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldCreateCustomer() throws Exception {

        mockMvc.perform(
                        post("/api/customers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                            {
                                "name": "Prem",
                                  "email": "premv1test20260826@gmail.com",
                                "phone": "9876543210"
                            }
                            """)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Prem"))
                .andExpect(jsonPath("$.email").value("premv1test20260826@gmail.com"))
                .andExpect(jsonPath("$.phone").value("9876543210"));
    }

    @Test
    void shouldRejectDuplicateEmail() throws Exception {

        String email = "premkumar.cse34@gmail.com";

        String requestBody = """
                {
                    "name": "Prem",
                    "email": "%s",
                    "phone": "8610770689"
                }
                """.formatted(email);

        mockMvc.perform(
                        post("/api/customers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Email already registered"));
    }

    @Test
    void shouldRejectInvalidCustomerRequest() throws Exception{

        String requestBody = """
                {
                    "name": "",
                    "email": "invalid",
                    "phone": "8610770689"
                }
                """;
        mockMvc.perform(
                        post("/api/customers").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isBadRequest());

    }

}