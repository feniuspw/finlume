package com.finlume.unit.core.services

import com.finlume.core.command.CreateWalletCommand
import com.finlume.core.command.DepositCommand
import com.finlume.core.command.UpdateWalletCommand
import com.finlume.core.command.WithdrawCommand
import com.finlume.core.domain.Currency
import com.finlume.core.domain.Wallet
import com.finlume.core.repositories.WalletRepositoryPort
import com.finlume.core.services.WalletService
import com.finlume.core.services.exceptions.WalletNotFoundException
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime
import java.util.UUID

class WalletServiceTest {

    // Cria um mock para o repositório de wallet
    private val repository = mockk<WalletRepositoryPort>()

    // Injeta o mock na service
    private val walletService = WalletService(repository)

    // Cria um objeto Currency para ser usado na criação de wallets
    private val currency = Currency(1, "BRL", "Real Brasileiro", "R$")

    @Test
    fun `createWallet should create and return a wallet`() {
        // Arrange
        val createCommand = CreateWalletCommand(
            name = "Test Wallet",
            balance = 100.0,
            currencyId = 1
        )

        every { repository.save(any()) } answers { firstArg() }

        // Act
        val createdWallet = walletService.createWallet(createCommand)

        // Assert
        assertEquals("Test Wallet", createdWallet.name)
        assertEquals(100.0, createdWallet.getBalance())
        assertEquals("BRL", createdWallet.currency.code)
        verify(exactly = 1) { repository.save(any<Wallet>()) }
    }

    @Test
    fun `findWalletById should return wallet if exists`() {
        // Arrange
        val walletId = UUID.randomUUID()
        val wallet = Wallet(
            id = walletId,
            name = "Existing Wallet",
            balance = 200.0,
            currency = currency,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
        every { repository.findById(walletId) } returns wallet

        // Act
        val foundWallet = walletService.findWalletById(walletId)

        // Assert
        assertEquals("Existing Wallet", foundWallet.name)
        assertEquals(200.0, foundWallet.getBalance())
        verify(exactly = 1) { repository.findById(walletId) }
    }

    @Test
    fun `findWalletById should throw WalletNotFoundException if not found`() {
        // Arrange
        val walletId = UUID.randomUUID()
        every { repository.findById(walletId) } returns null

        // Act & Assert
        val exception = assertThrows<WalletNotFoundException> {
            walletService.findWalletById(walletId)
        }
        assertEquals("Wallet with ID $walletId not found", exception.message)
        verify(exactly = 1) { repository.findById(walletId) }
    }

    @Test
    fun `updateWallet should update wallet name`() {
        // Arrange
        val walletId = UUID.randomUUID()
        val originalWallet = Wallet(
            id = walletId,
            name = "Original Wallet",
            balance = 150.0,
            currency = currency,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
        val updateCommand = UpdateWalletCommand(
            id = walletId,
            name = "Updated Wallet"
        )
        every { repository.findById(walletId) } returns originalWallet
        every { repository.save(any()) } answers { firstArg() }

        // Act
        val updatedWallet = walletService.updateWallet(updateCommand)

        // Assert
        assertEquals("Updated Wallet", updatedWallet.name)
        assertEquals(150.0, updatedWallet.getBalance())
        verify(exactly = 1) { repository.findById(walletId) }
        verify(exactly = 1) { repository.save(any<Wallet>()) }
    }

    @Test
    fun `deleteWallet should call repository delete`() {
        // Arrange
        val walletId = UUID.randomUUID()
        every { repository.delete(walletId) } returns Unit

        // Act
        walletService.deleteWallet(walletId)

        // Assert
        verify(exactly = 1) { repository.delete(walletId) }
    }

    @Test
    fun `depositWallet should update wallet balance`() {
        // Arrange
        val walletId = UUID.randomUUID()
        val initialBalance = 100.0
        val wallet = Wallet(
            id = walletId,
            name = "Deposit Wallet",
            balance = initialBalance,
            currency = currency,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
        val depositCommand = DepositCommand(
            id = walletId,
            amount = 50.0
        )
        every { repository.findById(walletId) } returns wallet
        every { repository.save(any()) } answers { firstArg() }

        // Act
        walletService.depositWallet(depositCommand)

        // Assert
        assertEquals(150.0, wallet.getBalance())
        verify(exactly = 1) { repository.findById(walletId) }
        verify(exactly = 1) { repository.save(wallet) }
    }

    @Test
    fun `withdrawWallet should update wallet balance`() {
        // Arrange
        val walletId = UUID.randomUUID()
        val initialBalance = 200.0
        val wallet = Wallet(
            id = walletId,
            name = "Withdraw Wallet",
            balance = initialBalance,
            currency = currency,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
        val withdrawCommand = WithdrawCommand(
            id = walletId,
            amount = 80.0
        )
        every { repository.findById(walletId) } returns wallet
        every { repository.save(any()) } answers { firstArg() }

        // Act
        walletService.withdrawWallet(withdrawCommand)

        // Assert
        assertEquals(120.0, wallet.getBalance())
        verify(exactly = 1) { repository.findById(walletId) }
        verify(exactly = 1) { repository.save(wallet) }
    }
}
