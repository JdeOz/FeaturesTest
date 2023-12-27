package com.test.featurestest.domain.repository

import com.test.featurestest.domain.model.Client

interface ClientRepository {
    fun getClienteById(clienteId: String): Client
}