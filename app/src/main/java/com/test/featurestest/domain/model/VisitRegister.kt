package com.test.featurestest.domain.model

import android.net.Uri

data class VisitRegister(
    val clienteId: Int,
    val vendedorId: Int,
    val rutaId: Int,
    val clienteAlmacenId: Int,
    val resultadoContactoId: Int,
    val resultado: String?,
    val foto1Uri: Uri?,
    val foto2Uri: Uri?,
    val comentario: String?,
    val latitud: String?,
    val longitud: String?

)
