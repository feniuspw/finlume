package com.finlume.core.domain

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class AssetAccountTest {

    private val user = User(email = "test@example.com", password = "password123")
    private val currency = Currency(code = "BRL", name = "Real Brasileiro", symbol = "R$")
    private val account = AssetAccount(user, currency, "Test Account", 100.0)
    private val otherAccount = AssetAccount(user, currency, "Test Other Account", 150.0)


    @Test
    fun `should throw when name is not provided`() {
        assertThrows(IllegalArgumentException::class.java) {
            AssetAccount(user, currency, "", 100.0)
        }
    }

    @Test
    fun `should make a deposit successfully`() {
        val amount = 80.0
        account.makeDeposit(amount)
        assertEquals(180.0, account.getBalance())
    }

    @Test
    fun `should throw when negative amount is provided on deposit`() {
        val amount = -1.0
        assertThrows(IllegalArgumentException::class.java) {
            account.makeDeposit(amount)
        }
    }

    @Test
    fun `should make transfer correctly`() {
        val amount = 80.0
        val result = account.transferTo(otherAccount, amount)
        assertEquals(result, true)
        assertEquals(account.getBalance(), 20.0)
        assertEquals(otherAccount.getBalance(), 230.0)
    }

    @Test
    fun `should throw when negative amount is provided on transfer`() {
        val amount = -1.0
        assertThrows(IllegalArgumentException::class.java) {
            account.transferTo(otherAccount, amount)
        }
    }

    @Test
    fun `balance can be negative`() {
        val amount = 200.0
        val result = account.transferTo(otherAccount, amount)
        assertEquals(result, true)
        assertEquals(account.getBalance(), -100.0)
        assertEquals(otherAccount.getBalance(), 350.0)
    }

    @Test
    fun `should format balance to correct currency`() {
        assertEquals(account.getFormatedBalance(), "R$ 100.0")
    }

    @Test
    fun `should get balance from account`() {
        assertEquals(account.getBalance(), 100.0)
    }

}