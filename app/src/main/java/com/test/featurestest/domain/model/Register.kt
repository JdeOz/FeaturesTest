package com.test.featurestest.domain.model

data class Register(
    val rutaId: Int,
    val clienteId: Int,
    val vendedorId: Int,
    val resultadoId: Int,
    val resultado: String,
    val telefono: String,
    val duracion: Long,
    val fecha: String,
    val comentario: String
){
    override fun toString(): String {
        return "Register(rutaId=$rutaId, clienteId=$clienteId, vendedorId=$vendedorId, resultadoId=$resultadoId, resultado=$resultado, telefono=$telefono, duracion=$duracion, fecha=$fecha, comentario=$comentario)"
    }
}