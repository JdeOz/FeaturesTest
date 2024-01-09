package com.test.featurestest.domain.model

data class BankAccount(
    val cuentaBancoId: Int,
    val bancoId: Int,
    val banco: String,
    val numeroCuenta: String,
    val monedaId: Int,
    val moneda: String
)