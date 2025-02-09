package com.finlume.core.gateways


import com.finlume.core.domain.Account
import com.finlume.core.command.CreateAccountCommand
import com.finlume.core.command.UpdateAccountCommand
import java.util.*

interface AccountPort {
    fun createAccount(request: CreateAccountCommand): Account
    fun findAccountById(id: UUID): Account
    fun findAllAccounts(): List<Account>?
    fun updateAccount(request: UpdateAccountCommand, id: UUID): Account
    fun deleteAccount(id: UUID)
}