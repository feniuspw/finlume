package com.finlume.core.gateways

import com.finlume.core.command.CreateSavingsAccountCommand
import com.finlume.core.command.DepositSavingsAccountCommand
import com.finlume.core.command.UpdateSavingsAccountCommand
import com.finlume.core.command.WithdrawSavingsAccountCommand
import com.finlume.core.domain.SavingsAccount
import java.util.*

interface SavingsAccountPort {
    fun createSavingsAccount(createCommand: CreateSavingsAccountCommand): SavingsAccount
    fun updateSavingsAccount(updateCommand: UpdateSavingsAccountCommand): SavingsAccount
    fun deleteSavingsAccount(id: UUID)
    fun findSavingsAccountById(id: UUID): SavingsAccount
    fun findSavingsAccountByAccountId(accountId: UUID): List<SavingsAccount>?
    fun deposit(depositCommand: DepositSavingsAccountCommand)
    fun withdraw(withdrawCommand: WithdrawSavingsAccountCommand)
    fun getBalance(id: UUID): Double
}