package com.finlume.unit.core.domain

import com.finlume.core.domain.Account
import com.finlume.core.domain.Currency
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.assertNotNull
import java.time.LocalDateTime
import org.junit.jupiter.api.Test


class AccountTest {

    @Test
    fun `should create account with default values`() {
        val currency = Currency(1, "BRL", "Real Brasileiro", "R$")
        val accountName = "Minha Conta"
        val accountDescription = "Conta para testes"

        val account = Account(
            name = accountName,
            currency = currency,
            description = accountDescription
        )
        val now = LocalDateTime.now()

        assertNotNull(account.id, "O ID não deve ser nulo")
        assertEquals(accountName, account.name, "O nome deve ser igual ao fornecido")
        assertEquals(currency, account.currency, "A moeda deve ser igual à fornecida")
        assertEquals(accountDescription, account.description, "A descrição deve ser igual à fornecida")
        assertTrue(account.active, "A conta deve estar ativa por padrão")

        assertTrue(account.creation.isBefore(now.plusSeconds(1)) && account.creation.isAfter(now.minusSeconds(1)),
            "O campo creation deve estar próximo do momento atual")
        assertTrue(account.lastUpdated.isBefore(now.plusSeconds(1)) && account.lastUpdated.isAfter(now.minusSeconds(1)),
            "O campo lastUpdated deve estar próximo do momento atual")
    }
}