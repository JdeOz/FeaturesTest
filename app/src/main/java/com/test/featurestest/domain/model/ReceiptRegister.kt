package com.test.featurestest.domain.model

data class ReceiptRegister(
    val clienteId: Int,
    val vendedorId: Int,
    val monedaId: Int,
    val nDocumento: Int?,
    val telefono: String,
    val condicion: String?,
    val importe: Double?,
    val fecha: String = ""

)
