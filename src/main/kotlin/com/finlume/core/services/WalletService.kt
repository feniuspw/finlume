package com.finlume.core.services

import com.finlume.core.command.CreateWalletCommand
import com.finlume.core.command.DepositCommand
import com.finlume.core.command.UpdateWalletCommand
import com.finlume.core.command.WithdrawCommand
import com.finlume.core.domain.Currency
import com.finlume.core.domain.Wallet
import com.finlume.core.gateways.WalletPort
import com.finlume.core.repositories.WalletRepositoryPort
import com.finlume.core.services.exceptions.WalletNotFoundException
import org.springframework.stereotype.Service
import java.util.*


@Service
class WalletService(
    val walletRepositoryPort: WalletRepositoryPort
): WalletPort {
    override fun createWallet(createCommand: CreateWalletCommand): Wallet {
        val currency = Currency(1, code = "BRL", name = "Real Brasileiro", symbol = "R$")
        val wallet = Wallet(
            name = createCommand.name,
            currency = currency,
            balance = createCommand.balance,
        )
        return walletRepositoryPort.save(wallet)
    }

    override fun findWalletById(id: UUID): Wallet {
        return walletRepositoryPort.findById(id)?: throw WalletNotFoundException("Wallet with ID $id not found")
    }

    override fun updateWallet(updateCommand: UpdateWalletCommand): Wallet {
        val wallet = this.findWalletById(updateCommand.id)
        val walletCopy = wallet.copy(
            name = updateCommand.name,
        )
        return walletRepositoryPort.save(walletCopy)
    }

    override fun deleteWallet(id: UUID) {
        walletRepositoryPort.delete(id)
    }

    override fun depositWallet(depositCommand: DepositCommand) {
        val wallet = this.findWalletById(depositCommand.id)
        wallet.deposit(depositCommand.amount)
        walletRepositoryPort.save(wallet)
    }

    override fun withdrawWallet(withdrawCommand: WithdrawCommand) {
        val wallet = this.findWalletById(withdrawCommand.id)
        wallet.withdraw(withdrawCommand.amount)
        walletRepositoryPort.save(wallet)
    }

}