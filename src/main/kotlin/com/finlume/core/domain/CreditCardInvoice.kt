package com.finlume.core.domain

import java.time.LocalDate

data class CreditCardInvoice (
    val account: CreditCardAccount,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val partialPayment: Boolean,
){
}