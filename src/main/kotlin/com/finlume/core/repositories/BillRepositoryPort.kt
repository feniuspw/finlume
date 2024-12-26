package com.finlume.core.repositories

import com.finlume.core.domain.Bill
import java.util.UUID

interface BillRepositoryPort {
    fun save(bill: Bill): Bill
    fun delete(id: UUID)
    fun findById(id: UUID): Bill?
}