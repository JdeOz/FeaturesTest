package com.test.featurestest.data.repository

import com.test.featurestest.domain.model.Client
import com.test.featurestest.domain.model.Direccion
import com.test.featurestest.domain.repository.ClientRepository

class ClientRepositoryImpl : ClientRepository {
    override fun getClienteById(clienteId: String): Client {
        return Client(
            clienteId = 1,
            empresaId = 1,
            personaId = 1,
            dCliente = "Client Number 1",
            nic = "990000$clienteId",
            codigo = "5533",
            telefono1 = "95123457",
            telefono2 = "957654321",
            telefonoSms = "",
            direcciones = listOf(
                Direccion(
                    personaAlmacenId = 1,
                    personaId = 1,
                    ubigeoId = 1,
                    ubigeo = "0400",
                    direccion = "Address Line 1",
                    latitud = "-16.500000",
                    longitud = "-16.300000"
                )
            ),
            rutaId = 1,
            deudaDias = 0
        )
    }
}