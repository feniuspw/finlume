package com.finlume.core.services

import com.finlume.core.domain.Bill
import com.finlume.core.gateways.BillPort
import com.finlume.core.repositories.BillRepositoryPort
import org.springframework.stereotype.Service
import java.util.*

@Service
class BillService(
    private val billRepositoryPort: BillRepositoryPort,
): BillPort {
    override fun createBill(bill: Bill): Bill {
        return billRepositoryPort.save(bill)
    }

    override fun updateBill(bill: Bill): Bill {

        return billRepositoryPort.save(bill)
    }

    override fun deleteBill(id: UUID) {
        billRepositoryPort.delete(id)
    }

    override fun getBill(id: UUID): Bill? {
        return billRepositoryPort.findById(id)
    }
}