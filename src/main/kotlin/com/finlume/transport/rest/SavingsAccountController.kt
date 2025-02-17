package com.finlume.transport.rest

import com.finlume.core.command.*
import com.finlume.core.domain.SavingsAccount
import com.finlume.core.gateways.AccountPort
import com.finlume.core.gateways.SavingsAccountPort
import com.finlume.transport.rest.dto.CreateSavingsAccountDTO
import com.finlume.transport.rest.dto.UpdateSavingsAccountBalanceDTO
import com.finlume.transport.rest.dto.UpdateSavingsAccountDTO
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController
@RequestMapping("/savings-accounts")
class SavingsAccountController(
    private val accountPort: AccountPort,
    private val savingsAccountPort: SavingsAccountPort
) {

    @PostMapping
    fun createSavingsAccount(@RequestBody request: CreateSavingsAccountDTO): ResponseEntity<SavingsAccount> {
        val account = accountPort.findAccountById(request.accountId)

        val createRequest = CreateSavingsAccountCommand(
            name = request.name,
            account = account,
            balance = request.balance,
            interestRate = request.interestRate,
            interestInterval = request.interestInterval,
        )

        val createdSavingsAccount = savingsAccountPort.createSavingsAccount(createRequest)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSavingsAccount)
    }

    @PatchMapping("/{id}")
    fun updateSavingsAccount(
        @PathVariable id: UUID,
        @RequestBody request: UpdateSavingsAccountDTO
    ): ResponseEntity<SavingsAccount> {
        val updateSavingsAccountCommand = UpdateSavingsAccountCommand(
            id = id,
            name = request.name,
            interestRate = request.interestRate,
            interestInterval = request.interestInterval,
        )
        return ResponseEntity.ok(savingsAccountPort.updateSavingsAccount(updateSavingsAccountCommand))
    }

    @DeleteMapping("{id}")
    fun deleteSavingsAccount(
        @PathVariable id: UUID
    ): ResponseEntity<Void> {
        savingsAccountPort.deleteSavingsAccount(id)
        return ResponseEntity.noContent().build()
    }


    @GetMapping("/{id}")
    fun findSavingsAccountById(
        @PathVariable id: UUID,
    ): ResponseEntity<SavingsAccount>? {
        return ResponseEntity.ok(savingsAccountPort.findSavingsAccountById(id))
    }

    @GetMapping("/account/{id}")
    fun findSavingsAccountByAccountId(
        @PathVariable id: UUID,
    ): ResponseEntity<List<SavingsAccount>>? {
        return ResponseEntity.ok(savingsAccountPort.findSavingsAccountByAccountId(id))
    }


    @GetMapping("/balance/{id}")
    fun getSavingsAccountBalance(
        @PathVariable id: UUID,
    ): ResponseEntity<Double>? {
        return ResponseEntity.ok(savingsAccountPort.getBalance(id))
    }

    @PostMapping("/deposit/{id}")
    fun depositAmountToSavingsAccount(
        @PathVariable id: UUID,
        @RequestBody request: UpdateSavingsAccountBalanceDTO
    ): ResponseEntity<Unit> {

        val depositSavingsAccountCommand = DepositSavingsAccountCommand(
            id = id,
            amount = request.amount,
        )
        return ResponseEntity.ok(savingsAccountPort.deposit(depositSavingsAccountCommand))
    }

    @PostMapping("/withdraw/{id}")
    fun withdrawAmountFromSavingsAccount(
        @PathVariable id: UUID,
        @RequestBody request: UpdateSavingsAccountBalanceDTO
    ): ResponseEntity<Unit> {

        val withdrawSavingsAccountCommand = WithdrawSavingsAccountCommand(
            id = id,
            amount = request.amount,
        )
        return ResponseEntity.ok(savingsAccountPort.withdraw(withdrawSavingsAccountCommand))
    }

}