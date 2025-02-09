package com.finlume.unit.core.services

import com.finlume.core.domain.Account
import com.finlume.core.domain.Currency
import com.finlume.core.command.CreateAccountCommand
import com.finlume.core.command.UpdateAccountCommand
import com.finlume.core.repositories.AccountRepositoryPort
import com.finlume.core.services.AccountService
import com.finlume.core.services.exceptions.AccountNotFoundException
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.slot
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime
import java.util.*


class AccountServiceTest {

    // Cria um mock para o repositório
    private val repository = mockk<AccountRepositoryPort>()

    // Injeta o mock na service
    private val accountService = AccountService(repository)


    @Test
    fun `createAccount deve criar e retornar uma account`() {
        // Arrange
        val currency = Currency(1, code = "BRL", name = "Real Brasileiro", symbol = "R$")

        val createDTO = CreateAccountCommand(
            name = "Conta Teste",
            description = "Descrição de teste",
            currency = currency
        )

        // Usamos um slot para capturar a Account passada para o repositório
        val accountSlot = slot<Account>()
        every { repository.create(capture(accountSlot)) } answers { firstArg() }

        // Act
        val accountCriada = accountService.createAccount(createDTO)

        // Assert
        assertEquals("Conta Teste", accountCriada.name)
        assertEquals("Descrição de teste", accountCriada.description)
        // Verifica se a moeda foi definida corretamente (conforme hard-coded na service)
        assertEquals("BRL", accountCriada.currency.code)
        // Verifica se o repositório foi chamado uma vez
        verify(exactly = 1) { repository.create(any()) }
    }

    @Test
    fun `findAccountById deve retornar a account se encontrada`() {
        // Arrange
        val accountId = UUID.randomUUID()
        val currency = Currency(1, code = "BRL", name = "Real Brasileiro", symbol = "R$")
        val accountEsperada = Account(
            id = accountId,
            name = "Conta Encontrada",
            description = "Descrição",
            currency = currency,
            active = true,
            creation = LocalDateTime.now(),
            lastUpdated = LocalDateTime.now()
        )
        every { repository.findById(accountId) } returns accountEsperada

        // Act
        val accountRetornada = accountService.findAccountById(accountId)

        // Assert
        assertNotNull(accountRetornada)
        assertEquals("Conta Encontrada", accountRetornada.name)
        verify(exactly = 1) { repository.findById(accountId) }
    }

    @Test
    fun `findAccountById should throw AccountNotFoundException if account is not found`() {
        // Arrange
        val accountId = UUID.randomUUID()
        every { repository.findById(accountId) } returns null

        // Act & Assert usando assertThrows com lambda envolvido em parênteses:
        val exception = assertThrows<AccountNotFoundException>{
            accountService.findAccountById(accountId)
        }

        // Verifica se a mensagem da exceção está conforme esperado:
        assertEquals("Account with id: $accountId not found", exception.message)
        verify(exactly = 1) { repository.findById(accountId) }
    }

    @Test
    fun `findAllAccounts deve retornar lista de accounts`() {
        // Arrange
        val currency = Currency(1, code = "BRL", name = "Real Brasileiro", symbol = "R$")
        val account1 = Account(
            id = UUID.randomUUID(),
            name = "Conta 1",
            description = "Desc 1",
            currency = currency,
            active = true,
            creation = LocalDateTime.now(),
            lastUpdated = LocalDateTime.now()
        )
        val account2 = Account(
            id = UUID.randomUUID(),
            name = "Conta 2",
            description = "Desc 2",
            currency = currency,
            active = true,
            creation = LocalDateTime.now(),
            lastUpdated = LocalDateTime.now()
        )
        every { repository.listAll() } returns listOf(account1, account2)

        // Act
        val contas = accountService.findAllAccounts()

        // Assert
        assertNotNull(contas)
        assertEquals(2, contas!!.size)
        verify(exactly = 1) { repository.listAll() }
    }

    @Test
    fun `should update account with new name and description`() {
        // Configura o cenário de teste
        val accountId = UUID.randomUUID()
        val existingAccount = Account(
            id = accountId,
            name = "Old Name",
            description = "Old Description",
            currency = Currency(1, "BRL", "Real Brasileiro", "R$"),
            active = true,
            creation = LocalDateTime.now(),
            lastUpdated = LocalDateTime.now()
        )
        val updateDTO = UpdateAccountCommand(
            name = "New Name",
            description = "New Description"
        )

        // Define o comportamento do mock
        every { repository.findById(accountId) } returns existingAccount
        every { repository.update(any()) } answers { firstArg() }

        // Executa a atualização
        val updatedAccount = accountService.updateAccount(updateDTO, accountId)

        // Verifica se a atualização ocorreu como esperado
        assertEquals("New Name", updatedAccount.name)
        assertEquals("New Description", updatedAccount.description)

        // Verifica se os métodos do repositório foram chamados
        verify(exactly = 1) { repository.findById(accountId) }
        verify(exactly = 1) { repository.update(any()) }
    }
}