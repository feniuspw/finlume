package com.finlume.web

import com.finlume.core.domain.Account
import com.finlume.core.domain.Currency
import com.finlume.core.command.CreateAccountCommand
import com.finlume.core.command.UpdateAccountCommand
import com.finlume.core.gateways.AccountPort
import com.finlume.transport.rest.dto.CreateAccountRequestDTO
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController
@RequestMapping("/accounts")
class AccountController(
    private val accountPort: AccountPort
) {

    @PostMapping
    fun createAccount(@RequestBody request: CreateAccountRequestDTO): ResponseEntity<Account> {
        val currency = Currency(1, code = "BRL", name = "Real Brasileiro", symbol = "R$")
        val createRequest = CreateAccountCommand(
            name = request.name,
            currency = currency,
            description = request.description
        )
        val createdAccount = accountPort.createAccount(createRequest)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAccount)
    }

    @GetMapping
    fun findAllAccounts(): ResponseEntity<List<Account>?> {
        return ResponseEntity.ok(accountPort.findAllAccounts())
    }

    @GetMapping("/{id}")
    fun findAccountById(
        @PathVariable id: UUID,
    ): ResponseEntity<Account>? {
        return ResponseEntity.ok(accountPort.findAccountById(id))
    }

    @PatchMapping("/{id}")
    fun updateAccount(
        @PathVariable id: UUID,
        @RequestBody request: UpdateAccountCommand
    ): ResponseEntity<Account> {

        return ResponseEntity.ok(accountPort.updateAccount(request, id))
    }

    @DeleteMapping("{id}")
    fun deleteAccount(
        @PathVariable id: UUID
    ): ResponseEntity<Void> {
        accountPort.deleteAccount(id)
        return ResponseEntity.noContent().build()
    }
}

