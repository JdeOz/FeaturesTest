package com.test.featurestest.util

import com.test.featurestest.data.network.model.ClientApiModel
import com.test.featurestest.data.network.model.Direccion
import com.test.featurestest.domain.model.Client
import com.test.featurestest.domain.model.Direction

fun mapDireccionToDirection(direccion: Direccion): Direction {
    return Direction(
        personaAlmacenId = direccion.personaAlmacenId,
        personaId = direccion.personaId,
        ubigeoId = direccion.ubigeoId,
        ubigeo = direccion.ubigeo,
        direccion = direccion.direccion,
        latitud = direccion.latitud,
        longitud = direccion.longitud
    )
}

fun mapClientApiModelToClient(clientApiModel: ClientApiModel): Client {
    return Client(
        clienteId = clientApiModel.clienteId,
        empresaId = clientApiModel.empresaId,
        personaId = clientApiModel.personaId,
        dCliente = clientApiModel.dCliente,
        nic = clientApiModel.nic,
        codigo = clientApiModel.codigo,
        telefono1 = clientApiModel.telefono1,
        telefono2 = clientApiModel.telefono2,
        telefonoSms = clientApiModel.telefonoSms,
        direcciones = clientApiModel.direcciones.map { mapDireccionToDirection(it) },
        rutaId = clientApiModel.rutaId,
        deudaDias = clientApiModel.deudaDias
    )
}
