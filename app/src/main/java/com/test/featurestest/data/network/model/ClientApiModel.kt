package com.test.featurestest.data.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ClientApiModel(
    @Json(name = "clienteId")
    val clienteId: Int,

    @Json(name = "empresaId")
    val empresaId: Int,

    @Json(name = "personaId")
    val personaId: Int,

    @Json(name = "dCliente")
    val dCliente: String,

    @Json(name = "nic")
    val nic: String,

    @Json(name = "codigo")
    val codigo: String,

    @Json(name = "telefono1")
    val telefono1: String,

    @Json(name = "telefono2")
    val telefono2: String,

    @Json(name = "telefonoSms")
    val telefonoSms: String,

    @Json(name = "direcciones")
    val direcciones: List<Direccion>,

    @Json(name = "rutaId")
    val rutaId: Int,

    @Json(name = "deudaDias")
    val deudaDias: Int
)

@JsonClass(generateAdapter = true)
data class Direccion(
    @Json(name = "personaAlmacenId")
    val personaAlmacenId: Int,

    @Json(name = "personaId")
    val personaId: Int,

    @Json(name = "ubigeoId")
    val ubigeoId: Int,

    @Json(name = "ubigeo")
    val ubigeo: String,

    @Json(name = "direccion")
    val direccion: String,

    @Json(name = "latitud")
    val latitud: String,

    @Json(name = "longitud")
    val longitud: String
)
