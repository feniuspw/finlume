package com.finlume.infrastructure.external.database.memory

import com.finlume.core.domain.AssetAccount
import com.finlume.core.domain.Bill
import com.finlume.core.repositories.BillRepositoryPort
import org.springframework.stereotype.Component
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Component
class InMemoryBillRepositoryAdapter: BillRepositoryPort {

    private val storage = ConcurrentHashMap<UUID, Bill>()

    override fun save(bill: Bill): Bill {
        storage[bill.id] = bill
        return bill
    }

    override fun findById(id: UUID): Bill? {
        return storage[id]
    }

    override fun delete(id: UUID) {
        storage.remove(id)
    }
}