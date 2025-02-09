package com.finlume.core.gateways

import com.finlume.core.command.CreateSavingsAccountCommand
import com.finlume.core.command.DepositSavingsAccountCommand
import com.finlume.core.command.UpdateSavingsAccountCommand
import com.finlume.core.command.WithdrawSavingsAccountCommand
import com.finlume.core.domain.SavingsAccount
import java.util.*

interface SavingsAccountPort {
    fun createSavingsAccount(createSavingsAccountCommand: CreateSavingsAccountCommand): SavingsAccount
    fun updateSavingsAccount(updateSavingsAccountCommand: UpdateSavingsAccountCommand)
    fun deleteSavingsAccount(id: UUID)
    fun findById(id: UUID): SavingsAccount?
    fun findByAccountId(accountId: UUID): List<SavingsAccount>?
    fun deposit(depositCommand: DepositSavingsAccountCommand)
    fun withdraw(withdrawSavingsAccountCommand: WithdrawSavingsAccountCommand)
    fun getBalance(id: UUID): Double
}