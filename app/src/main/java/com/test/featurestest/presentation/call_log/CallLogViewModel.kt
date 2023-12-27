package com.test.featurestest.presentation.call_log

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.featurestest.data.repository.ClientRepositoryImpl
import com.test.featurestest.domain.model.Client
import dagger.hilt.android.lifecycle.HiltViewModel

class CallLogViewModel : ViewModel() {
    private val clientRepository = ClientRepositoryImpl()

    private val _client = MutableLiveData<Client>()
    val client: LiveData<Client> = _client

    fun loadCliente(clienteId: String) {
        _client.value = clientRepository.getClienteById(clienteId)
    }
}