package com.finlume.web

import com.finlume.core.domain.AssetAccount
import com.finlume.core.domain.Bill
import com.finlume.core.domain.Currency
import com.finlume.core.domain.User
import com.finlume.core.gateways.BillPort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.util.*


@RestController
@RequestMapping("/bills")
class BillController(
    private val billPort: BillPort
) {

    @PostMapping
    fun createBill(@RequestBody request: CreateOrUpdateBillRequest): ResponseEntity<Bill> {
        val user = User(email = "test@example.com", password = "password123")
        val currency = Currency(1, code = "BRL", name = "Real Brasileiro", symbol = "R$")
        val bill = Bill(
            user = user,
            currency = currency,
            name = request.name,
            minAmount = request.minAmount,
            maxAmount = request.maxAmount,
            paymentDate = request.paymentDate,
            repeatInterval = request.repeatInterval,
        )
        val createdBill = billPort.createBill(bill)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBill)
    }

    @GetMapping("/{id}")
    fun getBill(@PathVariable id: UUID): ResponseEntity<Bill> {
        val bill = billPort.getBill(id) ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        return ResponseEntity.ok(bill)
    }

    @PutMapping("/{id}")
    fun updateBill(@PathVariable id: UUID, @RequestBody request: CreateOrUpdateBillRequest): ResponseEntity<Bill> {
        val user = User(email = "test@example.com", password = "password123")
        val currency = Currency(1, code = "BRL", name = "Real Brasileiro", symbol = "R$")
        val updateBill = Bill(
            id = id,
            user = user,
            currency = currency,
            name = request.name,
            minAmount = request.minAmount,
            maxAmount = request.maxAmount,
            paymentDate = request.paymentDate,
            repeatInterval = request.repeatInterval,
        )
        val updatedBill = billPort.updateBill(updateBill) ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        return ResponseEntity.ok(updatedBill)
    }

    @DeleteMapping("/{id}")
    fun deleteBill(@PathVariable id: UUID): ResponseEntity<Void> {
        val billExists = billPort.getBill(id) != null
        if (!billExists) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }

        billPort.deleteBill(id)
        return ResponseEntity.noContent().build()
    }

}

data class CreateOrUpdateBillRequest(
    val minAmount: Long,
    val maxAmount: Long,
    val name: String,
    val description: String = "",
    val paymentDate: LocalDate,
    val repeatInterval: Bill.RepeatInterval
)
