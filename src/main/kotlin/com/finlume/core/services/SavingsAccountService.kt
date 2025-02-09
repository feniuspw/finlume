package com.finlume.core.services
import com.finlume.core.command.CreateSavingsAccountCommand
import com.finlume.core.command.DepositSavingsAccountCommand
import com.finlume.core.command.UpdateSavingsAccountCommand
import com.finlume.core.command.WithdrawSavingsAccountCommand
import com.finlume.core.domain.SavingsAccount
import com.finlume.core.gateways.SavingsAccountPort
import com.finlume.core.repositories.SavingsAccountRepositoryPort
import org.springframework.stereotype.Service
import java.util.*

@Service
class SavingsAccountService(
//    private val savingsAccountRepository: SavingsAccountRepositoryPort
): SavingsAccountPort{
    override fun createSavingsAccount(createSavingsAccountCommand: CreateSavingsAccountCommand): SavingsAccount {

        val savingsAccount = SavingsAccount(
            name = createSavingsAccountCommand.name,
            balance = createSavingsAccountCommand.balance,
            account = createSavingsAccountCommand.account,
            interestRate = createSavingsAccountCommand.interestRate,
            interestInterval = createSavingsAccountCommand.interestInterval,
        )
//        return savingsAccountRepository.save(savingsAccount)
        return savingsAccount
    }

    override fun updateSavingsAccount(updateSavingsAccountCommand: UpdateSavingsAccountCommand) {
        TODO("Not yet implemented")
    }

    override fun deleteSavingsAccount(id: UUID) {
        TODO("Not yet implemented")
    }

    override fun findById(id: UUID): SavingsAccount? {
        TODO("Not yet implemented")
    }

    override fun findByAccountId(accountId: UUID): List<SavingsAccount>? {
        TODO("Not yet implemented")
    }

    override fun deposit(depositCommand: DepositSavingsAccountCommand) {
        TODO("Not yet implemented")
    }

    override fun withdraw(withdrawSavingsAccountCommand: WithdrawSavingsAccountCommand) {
        TODO("Not yet implemented")
    }

    override fun getBalance(id: UUID): Double {
        TODO("Not yet implemented")
    }
}