package com.test.featurestest.domain.model

data class Direction(
    val personaAlmacenId: Int,
    val personaId: Int,
    val ubigeoId: Int,
    val ubigeo: String,
    val direccion: String,
    val latitud: String,
    val longitud: String
)