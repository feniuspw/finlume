package com.finlume.core.gateways

import com.finlume.core.domain.Bill
import java.util.*

interface BillPort {
    fun createBill(bill: Bill): Bill
    fun updateBill(bill: Bill): Bill
    fun deleteBill(id: UUID)
    fun getBill(id: UUID): Bill?
}