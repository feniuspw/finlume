package com.finlume.transport.rest

import com.finlume.core.command.*
import com.finlume.core.domain.Wallet
import com.finlume.core.gateways.WalletPort
import com.finlume.transport.rest.dto.CreateWalletDTO
import com.finlume.transport.rest.dto.UpdateBalanceDTO
import com.finlume.transport.rest.dto.UpdateWalletDTO
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController
@RequestMapping("/wallet")
class WalletController(
    private val walletPort: WalletPort
) {

    @PostMapping
    fun createWallet(
        @RequestBody request: CreateWalletDTO
    ): ResponseEntity<Wallet> {
        val walletCommand = CreateWalletCommand(
            name = request.name,
            balance = request.balance,
            currencyId = request.currencyId
        )
        return ResponseEntity.status(HttpStatus.CREATED).body(walletPort.createWallet(walletCommand))
    }

    @GetMapping("/{id}")
    fun findWalletById(@PathVariable id: UUID): ResponseEntity<Wallet>? {
        return ResponseEntity.ok(walletPort.findWalletById(id))
    }

    @PatchMapping("/{id}")
    fun updateWallet(
        @PathVariable id: UUID,
        @RequestBody request: UpdateWalletDTO
    ): ResponseEntity<Wallet> {
        val updateWalletCommand = UpdateWalletCommand(
            id = id,
            name = request.name,
        )
        return ResponseEntity.ok(walletPort.updateWallet(updateWalletCommand))
    }

    @DeleteMapping("/{id}")
    fun deleteWallet(
        @PathVariable id: UUID
    ): ResponseEntity<Void> {
        walletPort.deleteWallet(id)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/deposit/{id}")
    fun depositAmountToWallet(
        @PathVariable id: UUID,
        @RequestBody request: UpdateBalanceDTO
    ): ResponseEntity<Unit> {

        val depositWalletCommand = DepositCommand(
            id = id,
            amount = request.amount,
        )
        return ResponseEntity.ok(walletPort.depositWallet(depositWalletCommand))
    }

    @PostMapping("/withdraw/{id}")
    fun withdrawAmountFromWallet(
        @PathVariable id: UUID,
        @RequestBody request: UpdateBalanceDTO
    ): ResponseEntity<Unit> {

        val withdrawWalletCommand = WithdrawCommand(
            id = id,
            amount = request.amount,
        )
        return ResponseEntity.ok(walletPort.withdrawWallet(withdrawWalletCommand))
    }


}