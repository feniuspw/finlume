package com.finlume.core.services
import com.finlume.core.command.CreateSavingsAccountCommand
import com.finlume.core.command.DepositSavingsAccountCommand
import com.finlume.core.command.UpdateSavingsAccountCommand
import com.finlume.core.command.WithdrawSavingsAccountCommand
import com.finlume.core.domain.SavingsAccount
import com.finlume.core.gateways.SavingsAccountPort
import com.finlume.core.repositories.SavingsAccountRepositoryPort
import com.finlume.core.services.exceptions.SavingsAccountNotFoundException
import org.springframework.stereotype.Service
import java.util.*

@Service
class SavingsAccountService(
   private val savingsAccountRepository: SavingsAccountRepositoryPort
): SavingsAccountPort{
    override fun createSavingsAccount(createCommand: CreateSavingsAccountCommand): SavingsAccount {

        val savingsAccount = SavingsAccount(
            name = createCommand.name,
            balance = createCommand.balance,
            account = createCommand.account,
            interestRate = createCommand.interestRate,
            interestInterval = createCommand.interestInterval,
        )
       return savingsAccountRepository.save(savingsAccount)
    }

    override fun updateSavingsAccount(updateCommand: UpdateSavingsAccountCommand): SavingsAccount {
        val savingsAccount = this.findSavingsAccountById(updateCommand.id)
        val updatedAccount = savingsAccount.copy(
            name = updateCommand.name ?: savingsAccount.name,
            interestRate = updateCommand.interestRate ?: savingsAccount.getInterestRate(),
            interestInterval = updateCommand.interestInterval ?: savingsAccount.getInterestInterval(),
        )
        return savingsAccountRepository.save(updatedAccount)
    }

    override fun deleteSavingsAccount(id: UUID) {
        savingsAccountRepository.delete(id)
    }

    override fun findSavingsAccountById(id: UUID): SavingsAccount {
        return savingsAccountRepository.findById(id)?: throw SavingsAccountNotFoundException("SavingsAccount with ID $id not found") 
    }

    override fun findSavingsAccountByAccountId(accountId: UUID): List<SavingsAccount>? {
        return savingsAccountRepository.findByAccountId(accountId)
    }

    override fun deposit(depositCommand: DepositSavingsAccountCommand) {
        val savingsAccount = this.findSavingsAccountById(depositCommand.id)
        savingsAccount.deposit(depositCommand.amount)
        savingsAccountRepository.save(savingsAccount)
    }

    override fun withdraw(withdrawCommand: WithdrawSavingsAccountCommand) {
        val savingsAccount = this.findSavingsAccountById(withdrawCommand.id)
        savingsAccount.withdraw(withdrawCommand.amount)
        savingsAccountRepository.save(savingsAccount)
    }

    override fun getBalance(id: UUID): Double {
        val savingsAccount = this.findSavingsAccountById(id)
        return savingsAccount.getBalance()
    }
}