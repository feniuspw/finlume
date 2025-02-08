package com.finlume.web

import com.finlume.core.domain.Account
import com.finlume.core.dto.account.CreateAccountDTO
import com.finlume.core.dto.account.UpdateAccountDTO
import com.finlume.core.gateways.AccountPort
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
    fun createAccount(@RequestBody request: CreateAccountDTO): ResponseEntity<Account> {
        val createdAccount = accountPort.createAccount(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAccount)
    }

    @GetMapping
    fun findAllAccounts(): ResponseEntity<List<Account>> {
        return ResponseEntity.status(HttpStatus.OK).body(accountPort.findAllAccounts())
    }

    @GetMapping("/{id}")
    fun findAccountById(
        @PathVariable id: UUID,
    ): ResponseEntity<Account>? {
        return accountPort.findAccountById(id)?.let { ResponseEntity.ok(it) } ?: ResponseEntity.notFound().build()
    }

    @PatchMapping("/{id}")
    fun updateAccount(
        @PathVariable id: UUID,
        @RequestBody request: UpdateAccountDTO
    ): ResponseEntity<Account> {

        return ResponseEntity.status(HttpStatus.OK).body(accountPort.updateAccount(request, id))
    }

    @DeleteMapping("{id}")
    fun deleteAccount(
        @PathVariable id: UUID
    ): ResponseEntity<Void> {
        accountPort.deleteAccount(id)
        return ResponseEntity.noContent().build()
    }
}

