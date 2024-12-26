package com.finlume.infrastructure.external.database.memory

import com.finlume.core.domain.Bill
import com.finlume.core.repositories.BillRepositoryPort
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class InMemoryBillRepositoryAdapter: BillRepositoryPort {

    private val storage = mutableMapOf<UUID, Bill>()

    override fun save(bill: Bill): Bill {
        storage[bill.id] = bill
        return bill
    }

    override fun findById(id: UUID): Bill? {
        return storage[id]
    }

    override fun findByUserId(userId: UUID): List<Bill>? {
        val relatedBills = ArrayList<Bill>()

        storage.forEach { (key, value) ->
            if (value.user.id == userId) {
                relatedBills.add(value)
            }
        }

        return relatedBills
    }

    override fun delete(id: UUID) {
        storage.remove(id)
    }
}