package com.finlume.integration

import com.fasterxml.jackson.databind.ObjectMapper
import com.finlume.core.domain.Wallet
import com.finlume.transport.rest.dto.CreateWalletDTO
import com.finlume.transport.rest.dto.UpdateBalanceDTO
import com.finlume.transport.rest.dto.UpdateWalletDTO
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
class WalletControllerIntegrationTest @Autowired constructor(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper
) {

    private lateinit var testWalletId: UUID

    @BeforeEach
    fun setup() {
        // Cria uma Wallet via endpoint POST /wallet
        val createWalletDTO = CreateWalletDTO(
            name = "Test Wallet",
            balance = 100.0,
            currencyId = 1
        )
        val result = mockMvc.perform(
            post("/wallet")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createWalletDTO))
        )
            .andExpect(status().isCreated)
            .andReturn()

        val createdWallet = objectMapper.readValue(result.response.contentAsString, Wallet::class.java)
        testWalletId = createdWallet.id
    }

    @AfterEach
    fun cleanup() {
        // Remove a Wallet criada para evitar interferência entre os testes.
        mockMvc.perform(delete("/wallet/{id}", testWalletId))
            .andExpect(status().isNoContent)
    }

    @Test
    fun `GET wallet by id returns the wallet when found`() {
        mockMvc.perform(get("/wallet/{id}", testWalletId))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(testWalletId.toString()))
            .andExpect(jsonPath("$.name").value("Test Wallet"))
    }

    @Test
    fun `PATCH update wallet updates only provided fields`() {
        // Atualiza apenas o campo name
        val updateDTO = UpdateWalletDTO(
            name = "Updated Wallet"
        )
        mockMvc.perform(
            patch("/wallet/{id}", testWalletId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("Updated Wallet"))
    }

    @Test
    fun `POST deposit updates wallet balance`() {
        val depositDTO = UpdateBalanceDTO(
            amount = 50.0
        )
        // Executa depósito
        mockMvc.perform(
            post("/wallet/deposit/{id}", testWalletId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(depositDTO))
        )
            .andExpect(status().isOk)

        // Verifica o novo saldo (deverá ser 150.0)
        mockMvc.perform(get("/wallet/{id}", testWalletId))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.balance").value(150.0))
    }

    @Test
    fun `POST withdraw updates wallet balance`() {
        val withdrawDTO = UpdateBalanceDTO(
            amount = 40.0
        )
        // Executa saque
        mockMvc.perform(
            post("/wallet/withdraw/{id}", testWalletId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(withdrawDTO))
        )
            .andExpect(status().isOk)

        // Verifica o novo saldo (100.0 - 40.0 = 60.0)
        mockMvc.perform(get("/wallet/{id}", testWalletId))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.balance").value(60.0))
    }

    @Test
    fun `DELETE wallet deletes the wallet`() {
        // Realiza o delete
        mockMvc.perform(delete("/wallet/{id}", testWalletId))
            .andExpect(status().isNoContent)

        // Verifica que a wallet não é encontrada (supondo que o endpoint GET retorne 404)
//        mockMvc.perform(get("/wallet/{id}", testWalletId))
//            .andExpect(status().isNotFound)
    }
}