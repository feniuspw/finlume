package com.finlume.integration

import com.fasterxml.jackson.databind.ObjectMapper
import com.finlume.core.domain.Account
import com.finlume.core.domain.SavingsAccount
import com.finlume.transport.rest.dto.CreateAccountRequestDTO
import com.finlume.transport.rest.dto.CreateSavingsAccountDTO
import com.finlume.transport.rest.dto.UpdateBalanceDTO
import com.finlume.transport.rest.dto.UpdateSavingsAccountDTO
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.util.*

@SpringBootTest
@AutoConfigureMockMvc
class SavingsAccountControllerIntegrationTest @Autowired constructor(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper
) {

    // Vamos criar uma conta para usar nos testes.
    lateinit var testAccountId: UUID
    lateinit var testSavingsAccountId: UUID

    @BeforeEach
    fun setup() {
        val createAccountDTO = CreateAccountRequestDTO(
            name = "Test Account",
            description = "Integration test account",
            currencyCode = 1
        )
        val result = mockMvc.perform(
            post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createAccountDTO))
        )
            .andExpect(status().isCreated)
            .andReturn()

        val createdAccount = objectMapper.readValue(result.response.contentAsString, Account::class.java)
        testAccountId = createdAccount.id

        val createSavingsAccountDTO = CreateSavingsAccountDTO(
            accountId = testAccountId,
            name = "Test Savings Account",
            balance = 100.0,
            interestRate = 0.05,
            interestInterval = 30
        )

        val savingsResult = mockMvc.perform(
            post("/savings-accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createSavingsAccountDTO))
        )
            .andExpect(status().isCreated)
            .andReturn()

        val createdSavingsAccount = objectMapper.readValue(savingsResult.response.contentAsString, SavingsAccount::class.java)
        testSavingsAccountId = createdSavingsAccount.id
    }

    @AfterEach
    fun cleanup() {
        // Remove a conta criada para evitar interferência entre os testes.
        mockMvc.perform(delete("/accounts/$testAccountId"))
            .andExpect(status().isNoContent)

        mockMvc.perform(delete("/savings-accounts/$testSavingsAccountId"))
            .andExpect(status().isNoContent)
    }

    @Test
    fun `GET savings account by id returns the savings account when found`() {
        mockMvc.perform(get("/savings-accounts/{id}", testSavingsAccountId))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(testSavingsAccountId.toString()))
            .andExpect(jsonPath("$.name").value("Test Savings Account"))
    }

    @Test
    fun `GET savings account by account id returns a list`() {
        mockMvc.perform(get("/savings-accounts/account/{id}", testAccountId))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$[0].id").value(testSavingsAccountId.toString()))
    }

    @Test
    fun `GET savings account balance returns correct balance`() {
        mockMvc.perform(get("/savings-accounts/balance/{id}", testSavingsAccountId))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").value(100.0))
    }

    @Test
    fun `PATCH update savings account updates only provided fields`() {
        // Atualiza apenas o campo name e interestRate (preserva interestInterval)
        val updateDTO = UpdateSavingsAccountDTO(
            name = "Updated Savings Account",
            interestRate = 0.07,
            interestInterval = null
        )
        mockMvc.perform(
            patch("/savings-accounts/{id}", testSavingsAccountId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("Updated Savings Account"))
            .andExpect(jsonPath("$.interestRate").value(0.07))
    }

    @Test
    fun `POST deposit updates savings account balance`() {
        val depositDTO = UpdateBalanceDTO(
            amount = 50.0
        )
        // Executa depósito
        mockMvc.perform(
            post("/savings-accounts/deposit/{id}", testSavingsAccountId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(depositDTO))
        )
            .andExpect(status().isOk)

        // Verifica o novo saldo (deverá ser 150.0)
        mockMvc.perform(get("/savings-accounts/balance/{id}", testSavingsAccountId))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").value(150.0))
    }

    @Test
    fun `POST withdraw updates savings account balance`() {
        val withdrawDTO = UpdateBalanceDTO(
            amount = 40.0
        )
        // Executa saque
        mockMvc.perform(
            post("/savings-accounts/withdraw/{id}", testSavingsAccountId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(withdrawDTO))
        )
            .andExpect(status().isOk)

        // Verifica o novo saldo (100.0 - 40.0 = 60.0)
        mockMvc.perform(get("/savings-accounts/balance/{id}", testSavingsAccountId))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").value(60.0))
    }

}