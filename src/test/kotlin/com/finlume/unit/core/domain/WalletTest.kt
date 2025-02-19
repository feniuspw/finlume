package com.finlume.unit.core.domain

import com.finlume.core.domain.Currency
import com.finlume.core.domain.Wallet
import com.finlume.core.domain.exceptions.InvalidAmountException
import com.finlume.core.domain.exceptions.InsufficientFundsException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

import java.util.UUID

class WalletTest {

    private val dummyCurrency = Currency(1, "BRL", "Real Brasileiro", "R$")

    @Test
    fun `should create wallet successfully`() {
        val wallet = Wallet(
            name = "My Wallet",
            balance = 100.0,
            currency = dummyCurrency
        )
        assertEquals("My Wallet", wallet.name)
        assertEquals(100.0, wallet.getBalance())
    }

    @Test
    fun `should throw exception when creating wallet with blank name`() {
        val exception = assertFailsWith<IllegalArgumentException> {
            Wallet(
                name = "",
                balance = 100.0,
                currency = dummyCurrency
            )
        }
        assertEquals("Wallet name must not be blank", exception.message)
    }

    @Test
    fun `should throw exception when creating wallet with negative balance`() {
        val exception = assertFailsWith<IllegalArgumentException> {
            Wallet(
                name = "Test Wallet",
                balance = -50.0,
                currency = dummyCurrency
            )
        }
        assertEquals("Initial balance cannot be negative", exception.message)
    }

    @Test
    fun `deposit should increase balance`() {
        val wallet = Wallet(
            name = "Test Wallet",
            balance = 100.0,
            currency = dummyCurrency
        )
        val newBalance = wallet.deposit(50.0)
        assertEquals(150.0, newBalance)
        assertEquals(150.0, wallet.getBalance())
    }

    @Test
    fun `deposit should throw exception for non-positive amount`() {
        val wallet = Wallet(
            name = "Test Wallet",
            balance = 100.0,
            currency = dummyCurrency
        )
        // Valor zero
        val exceptionZero = assertFailsWith<InvalidAmountException> {
            wallet.deposit(0.0)
        }
        assertEquals("Deposit amount must be positive", exceptionZero.message)

        // Valor negativo
        val exceptionNegative = assertFailsWith<InvalidAmountException> {
            wallet.deposit(-10.0)
        }
        assertEquals("Deposit amount must be positive", exceptionNegative.message)
    }

    @Test
    fun `withdraw should decrease balance`() {
        val wallet = Wallet(
            name = "Test Wallet",
            balance = 100.0,
            currency = dummyCurrency
        )
        val newBalance = wallet.withdraw(40.0)
        assertEquals(60.0, newBalance)
        assertEquals(60.0, wallet.getBalance())
    }

    @Test
    fun `withdraw should throw exception for non-positive amount`() {
        val wallet = Wallet(
            name = "Test Wallet",
            balance = 100.0,
            currency = dummyCurrency
        )
        // Valor zero
        val exceptionZero = assertFailsWith<InvalidAmountException> {
            wallet.withdraw(0.0)
        }
        assertEquals("Withdrawal amount must be positive", exceptionZero.message)

        // Valor negativo
        val exceptionNegative = assertFailsWith<InvalidAmountException> {
            wallet.withdraw(-10.0)
        }
        assertEquals("Withdrawal amount must be positive", exceptionNegative.message)
    }

    @Test
    fun `withdraw should throw exception when amount exceeds balance`() {
        val wallet = Wallet(
            name = "Test Wallet",
            balance = 100.0,
            currency = dummyCurrency
        )
        val exception = assertFailsWith<InsufficientFundsException> {
            wallet.withdraw(150.0)
        }
        assertEquals("Insufficient funds: withdrawal amount must not exceed the available balance", exception.message)
    }
}