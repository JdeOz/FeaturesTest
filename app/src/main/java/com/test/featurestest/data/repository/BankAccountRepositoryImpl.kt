package com.test.featurestest.data.repository

import com.test.featurestest.domain.model.BankAccount
import com.test.featurestest.domain.repository.BankAccountRepository
import javax.inject.Inject

class BankAccountRepositoryImpl @Inject constructor(): BankAccountRepository {
    override fun getBankAccounts(): List<BankAccount> {

        return listOf(
            BankAccount(
                cuentaBancoId = 1,
                bancoId = 1,
                banco = "Banco BCP",
                numeroCuenta = "10-20501155-10",
                monedaId = 1,
                moneda = "S/"
            ),
            BankAccount(
                cuentaBancoId = 2,
                bancoId = 2,
                banco = "Banco BBVA",
                numeroCuenta = "20-20501155-10",
                monedaId = 0,
                moneda = "$"
            )
        )
    }

}