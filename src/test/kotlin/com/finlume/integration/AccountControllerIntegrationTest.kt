package com.finlume.integration

import com.fasterxml.jackson.databind.ObjectMapper
import com.finlume.core.domain.Account
import com.finlume.core.dto.account.CreateAccountDTO
import com.finlume.core.dto.account.UpdateAccountDTO
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
class AccountControllerIntegrationTest @Autowired constructor(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper
) {

    // Vamos criar uma conta para usar nos testes.
    lateinit var testAccountId: UUID

    @BeforeEach
    fun setup() {
        // Cria uma conta via endpoint POST para garantir que o fluxo completo seja testado.
        // (Assumindo que existe um endpoint POST /accounts que cria a conta e retorna status 201)
        val createAccountDTO = CreateAccountDTO(
            name = "Test Account",
            description = "Integration test account"
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
    }

    @AfterEach
    fun cleanup() {
        // Remove a conta criada para evitar interferência entre os testes.
        mockMvc.perform(delete("/accounts/$testAccountId"))
            .andExpect(status().isNoContent)
    }

    @Test
    fun `POST create account successfully`() {
        // Arrange: Cria o DTO com os dados para criação da conta.
        val createAccountDTO = CreateAccountDTO(
            name = "Integration Account",
            description = "Testing account creation via integration test"
        )

        // Act & Assert: Faz uma requisição POST para o endpoint e valida o retorno.
        mockMvc.perform(
            post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createAccountDTO))
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.name").value("Integration Account"))
            .andExpect(jsonPath("$.description").value("Testing account creation via integration test"))
    }

    @Test
    fun `GET all accounts returns a list`() {
        mockMvc.perform(get("/accounts"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$[0].id").exists())
    }

    @Test
    fun `GET account by id returns the account when found`() {
        mockMvc.perform(get("/accounts/{id}", testAccountId))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(testAccountId.toString()))
            .andExpect(jsonPath("$.name").value("Test Account"))
    }

    @Test
    fun `GET account by id returns 404 when account not found`() {
        val nonExistentId = UUID.randomUUID()
        mockMvc.perform(get("/accounts/{id}", nonExistentId))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `PATCH update account updates only provided fields`() {
        // Atualiza apenas o campo description (o name permanece o mesmo)
        val updateDTO = UpdateAccountDTO(
            name = null, // Não altera o nome
            description = "Updated Description"
        )
        mockMvc.perform(
            patch("/accounts/{id}", testAccountId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.description").value("Updated Description"))
            .andExpect(jsonPath("$.name").value("Test Account"))
    }

    @Test
    fun `DELETE account soft deletes the account`() {
        // Realiza o delete
        mockMvc.perform(delete("/accounts/{id}", testAccountId))
            .andExpect(status().isNoContent)

        // Verifica se a conta realmente não é encontrada (ou está inativa)
        mockMvc.perform(get("/accounts/{id}", testAccountId))
            .andExpect(jsonPath("$.active").value(false))
    }
}