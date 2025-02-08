package com.finlume.web

import com.finlume.core.domain.Account
import com.finlume.core.domain.Currency
import com.finlume.core.gateways.AccountPort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/accounts")
class AccountController(
    private val accountPort: AccountPort
) {

    @PostMapping
    fun createAccount(@RequestBody request: CreateAccountRequest): ResponseEntity<Account> {
        val currency = Currency(1, code = "BRL", name = "Real Brasileiro", symbol = "R$")
        val account = Account(
            currency = currency,
            name = request.name,
            description = request.description
        )
        val createdAccount = accountPort.createAccount(account)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAccount)
    }

    @GetMapping
    fun getAllAccounts(): ResponseEntity<List<Account>> {
        return ResponseEntity.status(HttpStatus.OK).body(accountPort.listAccounts())
    }
}

data class CreateAccountRequest(
    val name: String,
    val description: String = ""
)