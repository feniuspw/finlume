package com.finlume.core.domain

import com.finlume.core.domain.exceptions.InvalidAmountException
import com.finlume.core.domain.exceptions.InsufficientFundsException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import java.time.LocalDateTime
import java.util.UUID

// Dummy implementation of Account for testing purposes.
// Ajuste conforme a sua implementação real.
class SavingsAccountTest {
    private val currency = Currency(1, "BRL", "Real Brasileiro", "R$")
    private fun createDummyAccount(): Account = Account(id = UUID.randomUUID(), currency = currency, name = "Dummy Account")

    @Test
    fun `should create SavingsAccount successfully`() {
        val dummyAccount = createDummyAccount()
        val savingsAccount = SavingsAccount(
            account = dummyAccount,
            name = "Test Savings",
            balance = 100.0,
            interestRate = 0.05,
            interestInterval = 30,
            lastInterestRateDate = LocalDateTime.now().minusDays(1)
        )

        assertEquals("Test Savings", savingsAccount.name)
        assertEquals(100.0, savingsAccount.getBalance())
        assertEquals(0.05, savingsAccount.getInterestRate())
        assertEquals(30, savingsAccount.getInterestInterval())
    }

    @Test
    fun `deposit should increase balance for valid amount`() {
        val dummyAccount = createDummyAccount()
        val savingsAccount = SavingsAccount(
            account = dummyAccount,
            name = "Test Savings",
            balance = 100.0,
            interestRate = 0.05,
            interestInterval = 30,
            lastInterestRateDate = LocalDateTime.now().minusDays(1)
        )

        val newBalance = savingsAccount.deposit(50.0)
        assertEquals(150.0, newBalance)
        assertEquals(150.0, savingsAccount.getBalance())
    }

    @Test
    fun `deposit should throw InvalidAmountException for zero or negative amount`() {
        val dummyAccount = createDummyAccount()
        val savingsAccount = SavingsAccount(
            account = dummyAccount,
            name = "Test Savings",
            balance = 100.0,
            interestRate = 0.05,
            interestInterval = 30,
            lastInterestRateDate = LocalDateTime.now().minusDays(1)
        )

        // Testa valor zero
        val exceptionZero = assertFailsWith<InvalidAmountException> {
            savingsAccount.deposit(0.0)
        }
        assertEquals("Deposit amount must be positive", exceptionZero.message)

        // Testa valor negativo
        val exceptionNegative = assertFailsWith<InvalidAmountException> {
            savingsAccount.deposit(-10.0)
        }
        assertEquals("Deposit amount must be positive", exceptionNegative.message)
    }

    @Test
    fun `withdraw should decrease balance for valid amount`() {
        val dummyAccount = createDummyAccount()
        val savingsAccount = SavingsAccount(
            account = dummyAccount,
            name = "Test Savings",
            balance = 100.0,
            interestRate = 0.05,
            interestInterval = 30,
            lastInterestRateDate = LocalDateTime.now().minusDays(1)
        )

        val newBalance = savingsAccount.withdraw(40.0)
        assertEquals(60.0, newBalance)
        assertEquals(60.0, savingsAccount.getBalance())
    }

    @Test
    fun `withdraw should throw InvalidAmountException for zero or negative amount`() {
        val dummyAccount = createDummyAccount()
        val savingsAccount = SavingsAccount(
            account = dummyAccount,
            name = "Test Savings",
            balance = 100.0,
            interestRate = 0.05,
            interestInterval = 30,
            lastInterestRateDate = LocalDateTime.now().minusDays(1)
        )

        // Valor zero
        val exceptionZero = assertFailsWith<InvalidAmountException> {
            savingsAccount.withdraw(0.0)
        }
        assertEquals("Withdrawal amount must be positive", exceptionZero.message)

        // Valor negativo
        val exceptionNegative = assertFailsWith<InvalidAmountException> {
            savingsAccount.withdraw(-20.0)
        }
        assertEquals("Withdrawal amount must be positive", exceptionNegative.message)
    }

    @Test
    fun `withdraw should throw InsufficientFundsException when amount exceeds balance`() {
        val dummyAccount = createDummyAccount()
        val savingsAccount = SavingsAccount(
            account = dummyAccount,
            name = "Test Savings",
            balance = 100.0,
            interestRate = 0.05,
            interestInterval = 30,
            lastInterestRateDate = LocalDateTime.now().minusDays(1)
        )

        val exception = assertFailsWith<InsufficientFundsException> {
            savingsAccount.withdraw(150.0)
        }
        assertEquals("Insufficient funds: withdrawal amount must not exceed the available balance", exception.message)
    }

    @Test
    fun `calculateInterest should return correct interest value`() {
        val dummyAccount = createDummyAccount()
        val balance = 200.0
        val interestRate = 0.05
        val savingsAccount = SavingsAccount(
            account = dummyAccount,
            name = "Test Savings",
            balance = balance,
            interestRate = interestRate,
            interestInterval = 30,
            lastInterestRateDate = LocalDateTime.now().minusDays(1)
        )

        val expectedInterest = balance * interestRate
        assertEquals(expectedInterest, savingsAccount.calculateInterest())
    }
}