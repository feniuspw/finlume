package com.finlume.core.gateways


import com.finlume.core.domain.Account
import com.finlume.core.dto.account.CreateAccountDTO
import com.finlume.core.dto.account.UpdateAccountDTO
import java.util.*

interface AccountPort {
    fun createAccount(request: CreateAccountDTO): Account
    fun findAccountById(id: UUID): Account?
    fun findAllAccounts(): List<Account>?
    fun updateAccount(request: UpdateAccountDTO, id: UUID): Account
    fun deleteAccount(id: UUID)
}