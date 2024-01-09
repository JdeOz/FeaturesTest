package com.test.featurestest.domain.repository

import com.test.featurestest.domain.model.BankAccount

interface BankAccountRepository {
    fun getBankAccounts(): List<BankAccount>

}