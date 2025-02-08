package com.finlume.core.domain

import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

data class CreditCardAccount(
    val id: UUID = UUID.randomUUID(),
    val account: Account,
    val name: String,
    private var outstandingAmount: Double = 0.0, // Valor acumulado na fatura
    private val creditLimit: Double,             // Limite total do cartão
    private val dueDate: LocalDate,              // Data de vencimento da fatura
    val creation: LocalDateTime = LocalDateTime.now(),
    val lastUpdated: LocalDateTime = LocalDateTime.now()
) {
    init {
        require(name.isNotBlank()) { "Credit card account name must not be blank" }
        require(creditLimit > 0) { "Credit limit must be positive" }
        require(!dueDate.isBefore(LocalDate.now())) { "Due date cannot be in the past" }
    }

    fun getOutstandingAmount(): Double = outstandingAmount

    /**
     * Registra uma cobrança no cartão.
     * A cobrança só é permitida se não ultrapassar o limite de crédito.
     */
    fun charge(amount: Double) {
        require(amount > 0) { "Charge amount must be positive" }
        require(outstandingAmount + amount <= creditLimit) { "Charge exceeds credit limit" }
        outstandingAmount += amount
        // Aqui você pode atualizar lastUpdated se necessário
    }

    /**
     * Registra um pagamento na fatura.
     * Se o valor pago for maior que o débito, o saldo é zerado.
     */
    fun pay(amount: Double) {
        require(amount > 0) { "Payment amount must be positive" }
        outstandingAmount = if (amount > outstandingAmount) 0.0 else outstandingAmount - amount
        // Aqui você pode atualizar lastUpdated se necessário
    }

    /**
     * Calcula a multa por atraso, caso a data atual seja posterior à data de vencimento.
     * Exemplo: 2% do valor em atraso para cada dia de atraso.
     */
    fun calculateLateFee(currentDate: LocalDate = LocalDate.now()): Double {
        return if (currentDate.isAfter(dueDate)) {
            val daysOverdue = currentDate.toEpochDay() - dueDate.toEpochDay()
            outstandingAmount * 0.02 * daysOverdue
        } else {
            0.0
        }
    }
}