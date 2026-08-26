package com.prem.banking_management_system.accounts;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldCreateAccount() throws Exception{

        String requestBody= """
                {
                  "customerId": "1",
                  "accountType": "SAVINGS",
                  "initialBalance": 1000.00
                }
                """;
        mockMvc.perform(post("/api/accounts").contentType(MediaType.APPLICATION_JSON).content(requestBody)
        ).andExpect(status().isCreated());

    }
    @Test
    void shouldRejectAccountWhenCustomerDoesNotExist() throws Exception {

        String requestBody = """
            {
                "customerId": 999999,
                "accountType": "SAVINGS",
                "initialBalance": 1000.00
            }
            """;

        mockMvc.perform(
                        post("/api/accounts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Customer Not found"));
    }
    @Test
    void shouldRejectInvalidAccountRequest() throws Exception {

        String requestBody = """
            {
                "customerId": 1,
                "accountType": null,
                "initialBalance": -1000.00
            }
            """;

        mockMvc.perform(
                        post("/api/accounts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isBadRequest());
    }

}
