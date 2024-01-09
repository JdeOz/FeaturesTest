package com.test.featurestest.domain.model

data class CallRegister(
    val rutaId: Int,
    val clienteId: Int,
    val vendedorId: Int,
    val resultadoId: Int,
    val resultado: String,
    val telefono: String,
    val duracion: Long,
    val fecha: String,
    val comentario: String
)