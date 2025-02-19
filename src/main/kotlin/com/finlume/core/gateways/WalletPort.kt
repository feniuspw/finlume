package com.finlume.core.gateways

import com.finlume.core.command.CreateWalletCommand
import com.finlume.core.command.DepositCommand
import com.finlume.core.command.WithdrawCommand
import com.finlume.core.command.UpdateWalletCommand
import com.finlume.core.domain.Wallet
import java.util.*

interface WalletPort {
    fun createWallet(createCommand: CreateWalletCommand): Wallet
    fun findWalletById(id: UUID): Wallet
    fun updateWallet(updateCommand: UpdateWalletCommand): Wallet
    fun deleteWallet(id: UUID)
    fun depositWallet(depositCommand: DepositCommand)
    fun withdrawWallet(withdrawCommand: WithdrawCommand)
}