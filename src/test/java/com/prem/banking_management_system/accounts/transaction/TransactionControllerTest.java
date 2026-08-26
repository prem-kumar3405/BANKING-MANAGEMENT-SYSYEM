package com.prem.banking_management_system.accounts.transaction;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldDepositSuccessfully() throws Exception {

        String requestBody = """
                {
                    "accountId": 1,
                    "amount": 2000.00
                }
                """;

        mockMvc.perform(
                        post("/api/transactions/deposit")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accountId").value(1))
                .andExpect(jsonPath("$.transactionType").value("DEPOSIT"))
                .andExpect(jsonPath("$.amount").value(2000.00))
                .andExpect(jsonPath("$.status").value("SUCCESS"));
    }
    @Test
    void shouldWithdrawSuccessfully() throws Exception {

        String requestBody = """
            {
                "accountId": 1,
                "amount": 1000.00
            }
            """;

        mockMvc.perform(
                        post("/api/transactions/withdraw")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accountId").value(1))
                .andExpect(jsonPath("$.transactionType").value("WITHDRAWAL"))
                .andExpect(jsonPath("$.amount").value(1000.00))
                .andExpect(jsonPath("$.status").value("SUCCESS"));
    }

    @Test
    void shouldRejectWithdrawalWhenBalanceIsInsufficient() throws Exception {

        String requestBody = """
            {
                "accountId": 1,
                "amount": 1000000.00
            }
            """;

        mockMvc.perform(
                        post("/api/transactions/withdraw")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Insufficient balance"));
    }
    @Test
    void shouldRejectNegativeDepositAmount() throws Exception {

        String requestBody = """
            {
                "accountId": 1,
                "amount": -500.00
            }
            """;

        mockMvc.perform(
                        post("/api/transactions/deposit")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isBadRequest());
    }
    @Test
    void shouldRejectDepositWhenAccountDoesNotExist() throws Exception {

        String requestBody = """
            {
                "accountId": 999999,
                "amount": 1000.00
            }
            """;

        mockMvc.perform(
                        post("/api/transactions/deposit")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Account not found"));
    }
    @Test
    void shouldRejectWithdrawalWhenAccountDoesNotExist() throws Exception {

        String requestBody = """
            {
                "accountId": 999999,
                "amount": 1000.00
            }
            """;

        mockMvc.perform(
                        post("/api/transactions/withdraw")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Account Not  found"));
    }
}