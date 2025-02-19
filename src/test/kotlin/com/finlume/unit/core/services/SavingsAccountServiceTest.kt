package com.finlume.unit.core.services

import com.finlume.core.command.CreateSavingsAccountCommand
import com.finlume.core.command.DepositCommand
import com.finlume.core.command.UpdateSavingsAccountCommand
import com.finlume.core.command.WithdrawCommand
import com.finlume.core.domain.Account
import com.finlume.core.domain.Currency
import com.finlume.core.domain.SavingsAccount
import com.finlume.core.repositories.SavingsAccountRepositoryPort
import com.finlume.core.services.SavingsAccountService
import com.finlume.core.services.exceptions.SavingsAccountNotFoundException
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime
import java.util.UUID

class SavingsAccountServiceTest {

    // Cria um mock para o repositório
    private val repository = mockk<SavingsAccountRepositoryPort>()

    // Injeta o mock na service
    private val savingsAccountService = SavingsAccountService(repository)

    // Função auxiliar para criar um dummy Account (necessário para SavingsAccount)
    val currency = Currency(1, "BRL", "Real Brasileiro", "R$")
    private fun createDummyAccount(): Account = Account(id = UUID.randomUUID(), currency = currency, name = "Dummy Account")

    @Test
    fun `createSavingsAccount should create and return a savings account`() {
        // Arrange
        val dummyAccount = createDummyAccount()
        val createCommand = CreateSavingsAccountCommand(
            name = "Savings Test",
            account = dummyAccount,
            balance = 100.0,
            interestRate = 0.05,
            interestInterval = 30
        )

        // Configura o mock: quando o repositório salvar, retorna a própria entidade
        every { repository.save(any()) } answers { firstArg() }

        // Act
        val createdSavings = savingsAccountService.createSavingsAccount(createCommand)

        // Assert
        assertEquals("Savings Test", createdSavings.name)
        assertEquals(100.0, createdSavings.getBalance())
        assertEquals(0.05, createdSavings.getInterestRate())
        assertEquals(30, createdSavings.getInterestInterval())
        verify(exactly = 1) { repository.save(any<SavingsAccount>()) }
    }

    @Test
    fun `updateSavingsAccount should update only provided fields`() {
        // Arrange
        val dummyAccount = createDummyAccount()
        val savingsAccount = SavingsAccount(
            account = dummyAccount,
            name = "Old Savings",
            balance = 200.0,
            interestRate = 0.04,
            interestInterval = 30,
            lastInterestRateDate = LocalDateTime.now().minusDays(1)
        )
        val accountId = savingsAccount.id
        val updateCommand = UpdateSavingsAccountCommand(
            id = accountId,
            name = "New Savings",
            interestRate = 0.06,
            interestInterval = null  // não altera interestInterval
        )
        every { repository.findById(accountId) } returns savingsAccount
        every { repository.save(any()) } answers { firstArg() }

        // Act
        val updatedSavings = savingsAccountService.updateSavingsAccount(updateCommand)

        // Assert
        assertEquals("New Savings", updatedSavings.name)
        assertEquals(0.06, updatedSavings.getInterestRate())
        assertEquals(30, updatedSavings.getInterestInterval()) // permanece inalterado
        verify(exactly = 1) { repository.findById(accountId) }
        verify(exactly = 1) { repository.save(any<SavingsAccount>()) }
    }

    @Test
    fun `deleteSavingsAccount should call repository delete`() {
        // Arrange
        val accountId = UUID.randomUUID()
        every { repository.delete(accountId) } returns Unit

        // Act
        savingsAccountService.deleteSavingsAccount(accountId)

        // Assert
        verify(exactly = 1) { repository.delete(accountId) }
    }

    @Test
    fun `findSavingsAccountById should return savings account if exists`() {
        // Arrange
        val accountId = UUID.randomUUID()
        val dummyAccount = createDummyAccount()
        val savingsAccount = SavingsAccount(
            id = accountId,
            account = dummyAccount,
            name = "Test Savings",
            balance = 150.0,
            interestRate = 0.03,
            interestInterval = 30,
            lastInterestRateDate = LocalDateTime.now().minusDays(1)
        )
        every { repository.findById(accountId) } returns savingsAccount

        // Act
        val foundSavings = savingsAccountService.findSavingsAccountById(accountId)

        // Assert
        assertEquals("Test Savings", foundSavings.name)
        verify(exactly = 1) { repository.findById(accountId) }
    }

    @Test
    fun `findSavingsAccountById should throw SavingsAccountNotFoundException if not found`() {
        // Arrange
        val accountId = UUID.randomUUID()
        every { repository.findById(accountId) } returns null

        // Act & Assert
        val exception = assertThrows<SavingsAccountNotFoundException> {
            savingsAccountService.findSavingsAccountById(accountId)
        }
        assertEquals("SavingsAccount with ID $accountId not found", exception.message)
        verify(exactly = 1) { repository.findById(accountId) }
    }

    @Test
    fun `findSavingsAccountByAccountId should return list of savings accounts`() {
        // Arrange
        val dummyAccount = createDummyAccount()
        val savings1 = SavingsAccount(
            id = UUID.randomUUID(),
            account = dummyAccount,
            name = "Savings 1",
            balance = 100.0,
            interestRate = 0.05,
            interestInterval = 30,
            lastInterestRateDate = LocalDateTime.now().minusDays(1)
        )
        val savings2 = SavingsAccount(
            id = UUID.randomUUID(),
            account = dummyAccount,
            name = "Savings 2",
            balance = 200.0,
            interestRate = 0.04,
            interestInterval = 30,
            lastInterestRateDate = LocalDateTime.now().minusDays(1)
        )
        val accountId = dummyAccount.id
        every { repository.findByAccountId(accountId) } returns listOf(savings1, savings2)

        // Act
        val savingsList = savingsAccountService.findSavingsAccountByAccountId(accountId)

        // Assert
        assertEquals(2, savingsList?.size)
        verify(exactly = 1) { repository.findByAccountId(accountId) }
    }

    @Test
    fun `deposit should update savings account balance`() {
        // Arrange
        val accountId = UUID.randomUUID()
        val dummyAccount = createDummyAccount()
        val savingsAccount = SavingsAccount(
            id = accountId,
            account = dummyAccount,
            name = "Deposit Test",
            balance = 100.0,
            interestRate = 0.05,
            interestInterval = 30,
            lastInterestRateDate = LocalDateTime.now().minusDays(1)
        )
        val depositCommand = DepositCommand(
            id = accountId,
            amount = 50.0
        )
        every { repository.findById(accountId) } returns savingsAccount
        every { repository.save(any()) } answers { firstArg() }

        // Act
        savingsAccountService.deposit(depositCommand)

        // Assert
        assertEquals(150.0, savingsAccount.getBalance())
        verify(exactly = 1) { repository.findById(accountId) }
        verify(exactly = 1) { repository.save(savingsAccount) }
    }

    @Test
    fun `withdraw should update savings account balance`() {
        // Arrange
        val accountId = UUID.randomUUID()
        val dummyAccount = createDummyAccount()
        val savingsAccount = SavingsAccount(
            id = accountId,
            account = dummyAccount,
            name = "Withdrawal Test",
            balance = 200.0,
            interestRate = 0.05,
            interestInterval = 30,
            lastInterestRateDate = LocalDateTime.now().minusDays(1)
        )
        val withdrawCommand = WithdrawCommand(
            id = accountId,
            amount = 80.0
        )
        every { repository.findById(accountId) } returns savingsAccount
        every { repository.save(any()) } answers { firstArg() }

        // Act
        savingsAccountService.withdraw(withdrawCommand)

        // Assert
        assertEquals(120.0, savingsAccount.getBalance())
        verify(exactly = 1) { repository.findById(accountId) }
        verify(exactly = 1) { repository.save(savingsAccount) }
    }

    @Test
    fun `getBalance should return correct savings account balance`() {
        // Arrange
        val accountId = UUID.randomUUID()
        val dummyAccount = createDummyAccount()
        val savingsAccount = SavingsAccount(
            id = accountId,
            account = dummyAccount,
            name = "Balance Test",
            balance = 300.0,
            interestRate = 0.05,
            interestInterval = 30,
            lastInterestRateDate = LocalDateTime.now().minusDays(1)
        )
        every { repository.findById(accountId) } returns savingsAccount

        // Act
        val balance = savingsAccountService.getBalance(accountId)

        // Assert
        assertEquals(300.0, balance)
        verify(exactly = 1) { repository.findById(accountId) }
    }
}