package com.finlume.core.gateways


import com.finlume.core.domain.Account
import java.util.*

interface AccountPort {
    fun createAccount(account: Account): Account
    fun getAccount(id: UUID): Account?
    fun listAccounts(): List<Account>?
    fun updateAccount(account: Account): Account
    fun deleteAccount(id: UUID)
//    fun withdrawFromAccount(id: UUID, amount: Double): Boolean
//    fun depositToAccount(id: UUID, amount: Double): Boolean
//    fun transferBetweenAccounts(fromAccountID: UUID, toAccountID: UUID, amount: Double): Boolean
}