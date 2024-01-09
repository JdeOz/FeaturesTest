package com.test.featurestest.data.repository

import com.test.featurestest.domain.model.ReceiptRegister
import com.test.featurestest.domain.repository.ReceiptRepository
import javax.inject.Inject

class ReceiptRepositoryImpl @Inject constructor() : ReceiptRepository {
    override fun getReceipts(): List<ReceiptRegister> {

        return listOf(
            ReceiptRegister(1, 101, 1, 1234, "555-1234", "Contado", 500.0, "2024-01-09"),
            ReceiptRegister(2, 102, 2, null, "555-5678", "Crédito", null, "2024-01-10"),
            ReceiptRegister(3, 103, 1, 5678, "555-9876", "Contado", 800.0, "2024-01-11")
        )
    }
}