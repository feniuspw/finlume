package com.finlume.unit.core.domain.validators

import com.finlume.core.domain.exceptions.InsufficientFundsException
import com.finlume.core.domain.exceptions.InvalidAmountException
import com.finlume.core.domain.validators.validateCanDeposit
import com.finlume.core.domain.validators.validateCanWithdraw
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.test.assertFailsWith

class SavingsAccountValidatorTest {

    @Test
    fun `should throw exception when deposit is not positive`() {
        val exception = assertFailsWith<InvalidAmountException> {
            validateCanDeposit(0.0)
        }
        assertEquals("Deposit amount must be positive", exception.message)
        val exception2 = assertFailsWith<InvalidAmountException> {
            validateCanDeposit(-1.0)
        }
        assertEquals("Deposit amount must be positive", exception2.message)
    }

    @Test
    fun `should not throw exception when deposit is positive`() {
        validateCanDeposit(100.0) // No exception should be thrown
    }

    @Test
    fun `validateCanWithdraw should throw InvalidAmountException for non-positive amount`() {
        // Teste com valor zero
        val exceptionZero = assertFailsWith<InvalidAmountException> {
            validateCanWithdraw(0.0, 100.0)
        }
        assertEquals("Withdrawal amount must be positive", exceptionZero.message)

        // Teste com valor negativo
        val exceptionNegative = assertFailsWith<InvalidAmountException> {
            validateCanWithdraw(-50.0, 100.0)
        }
        assertEquals("Withdrawal amount must be positive", exceptionNegative.message)
    }

    @Test
    fun `validateCanWithdraw should throw InsufficientFundsException when balance is insufficient`() {
        // Teste com saldo zero
        val exceptionZeroBalance = assertFailsWith<InsufficientFundsException> {
            validateCanWithdraw(50.0, 0.0)
        }
        assertEquals("Insufficient funds: withdrawal amount must not exceed the available balance", exceptionZeroBalance.message)

        // Teste com valor de saque maior que o saldo
        val exceptionExceeds = assertFailsWith<InsufficientFundsException> {
            validateCanWithdraw(150.0, 100.0)
        }
        assertEquals("Insufficient funds: withdrawal amount must not exceed the available balance", exceptionExceeds.message)
    }

    @Test
    fun `validateCanWithdraw should not throw exception for valid withdrawal`() {
        // Cenário válido: valor positivo e menor ou igual ao saldo
        validateCanWithdraw(50.0, 100.0)
    }
}