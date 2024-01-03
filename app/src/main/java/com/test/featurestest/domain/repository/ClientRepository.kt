package com.test.featurestest.domain.repository

import com.test.featurestest.domain.model.Client

interface ClientRepository {
    suspend fun getClienteById(clienteId: String): Client

}