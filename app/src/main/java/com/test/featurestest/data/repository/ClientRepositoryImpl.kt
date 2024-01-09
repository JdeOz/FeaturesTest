package com.test.featurestest.data.repository

import com.test.featurestest.data.network.ClientService
import com.test.featurestest.domain.model.Client
import com.test.featurestest.domain.model.Direction
import com.test.featurestest.domain.repository.ClientRepository
import com.test.featurestest.util.mapClientApiModelToClient
import javax.inject.Inject

class ClientRepositoryImpl @Inject constructor() : ClientRepository {
    val api = ClientService()
    override suspend fun getClienteById(clienteId: String): Client {

        var client = Client(
            clienteId = 1,
            empresaId = 1,
            personaId = 1,
            dCliente = "Client Number 1",
            nic = "990000$clienteId",
            codigo = "5533",
            telefono1 = "",
            telefono2 = "987102848",
//            telefono1 = "95123457",
//            telefono2 = "957654321",
            telefonoSms = "",
            direcciones = listOf(
                Direction(
                    personaAlmacenId = 1,
                    personaId = 1,
                    ubigeoId = 1,
                    ubigeo = "0400",
                    direccion = "Address Line 1",
                    latitud = "-16.39862",
                    longitud = "-71.53662"
                )
            ),
            rutaId = 1,
            deudaDias = 0
        )
        val apiClient = api.getClient()
        if(apiClient != null) {
            client = mapClientApiModelToClient(apiClient)
        }
        return client
    }
}

