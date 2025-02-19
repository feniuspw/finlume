package com.finlume.core.gateways

import com.finlume.core.command.CreateSavingsAccountCommand
import com.finlume.core.command.DepositCommand
import com.finlume.core.command.UpdateSavingsAccountCommand
import com.finlume.core.command.WithdrawCommand
import com.finlume.core.domain.SavingsAccount
import java.util.*

interface SavingsAccountPort {
    fun createSavingsAccount(createCommand: CreateSavingsAccountCommand): SavingsAccount
    fun updateSavingsAccount(updateCommand: UpdateSavingsAccountCommand): SavingsAccount
    fun deleteSavingsAccount(id: UUID)
    fun findSavingsAccountById(id: UUID): SavingsAccount
    fun findSavingsAccountByAccountId(accountId: UUID): List<SavingsAccount>?
    fun deposit(depositCommand: DepositCommand)
    fun withdraw(withdrawCommand: WithdrawCommand)
    fun getBalance(id: UUID): Double
}