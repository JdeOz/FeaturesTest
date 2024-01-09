package com.test.featurestest.domain.repository

import com.test.featurestest.domain.model.ReceiptRegister

interface ReceiptRepository {
    fun getReceipts(): List<ReceiptRegister>

}