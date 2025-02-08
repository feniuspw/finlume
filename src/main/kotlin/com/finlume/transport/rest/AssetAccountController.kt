package com.finlume.web

import com.finlume.core.domain.AssetAccount
import com.finlume.core.domain.Currency
import com.finlume.core.domain.User
import com.finlume.core.gateways.AssetAccountPort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*


@RestController
@RequestMapping("/asset-accounts")
class AssetAccountController(
    private val assetAccountPort: AssetAccountPort
) {

    @PostMapping
    fun createAccount(@RequestBody request: CreateAssetAccountRequest): ResponseEntity<AssetAccount> {
        val user = User(id= UUID.fromString("c71bb851-1066-48a3-b474-22d29e7d9ecf"), email = "test@example.com", password = "password123")
        val currency = Currency(1, code = "BRL", name = "Real Brasileiro", symbol = "R$")
        val account = AssetAccount(
            user = user,
            currency = currency,
            name = request.name,
            balance = request.balance
        )
        val createdAccount = assetAccountPort.createAccount(account)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAccount)
    }

    @GetMapping
    fun getAllAccounts(): ResponseEntity<List<AssetAccount>> {
        return ResponseEntity.status(HttpStatus.OK).body(assetAccountPort.listMyAccounts())
    }
}

data class CreateAssetAccountRequest(
    val name: String,
    val balance: Double
)