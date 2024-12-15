package com.finlume.core.domain

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.util.UUID

class AssetAccountTest {
    private val id = UUID.randomUUID()
    private val currencyId = 1L
    private val user = User(email = "test@example.com", password = "password123")
    private val currency = Currency(currencyId, code = "BRL", name = "Real Brasileiro", symbol = "R$")
    private val account = AssetAccount(id, user, currency, "Test Account", 100.0)

    @Test
    fun `should throw when name is not provided`() {
        assertThrows(IllegalArgumentException::class.java) {
            AssetAccount(id, user, currency, "", 100.0)
        }
    }

    @Test
    fun `should make a deposit successfully`() {
        val amount = 80.0
        account.makeDeposit(amount)
        assertEquals(180.0, account.getBalance())
    }

    @Test
    fun `should make a withdraw successfully`() {
        val amount = 80.0
        account.makeWithdraw(amount)
        assertEquals(20.0, account.getBalance())
    }
    @Test
    fun `should throw when negative amount is provided on deposit`() {
        val amount = -1.0
        assertThrows(IllegalArgumentException::class.java) {
            account.makeDeposit(amount)
        }
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