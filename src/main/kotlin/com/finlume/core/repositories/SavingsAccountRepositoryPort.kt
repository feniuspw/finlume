package com.finlume.core.repositories

import com.finlume.core.domain.SavingsAccount

interface SavingsAccountRepositoryPort {
    fun save(savingsAccount: SavingsAccount): SavingsAccount
}