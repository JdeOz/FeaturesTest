package com.test.featurestest.domain.model

data class Client(
    val clienteId: Int,
    val empresaId: Int,
    val personaId: Int,
    val dCliente: String,
    val nic: String,
    val codigo: String,
    val telefono1: String,
    val telefono2: String,
    val telefonoSms: String,
    val direcciones: List<Direction>,
    val rutaId: Int,
    val deudaDias: Int
)

