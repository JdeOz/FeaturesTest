package com.test.featurestest.presentation.receipt

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.featurestest.data.repository.ClientRepositoryImpl
import com.test.featurestest.domain.model.Client
import com.test.featurestest.domain.model.Phone
import com.test.featurestest.domain.model.ReceiptRegister
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class ReceiptViewModel @Inject constructor(
    private val clientRepository: ClientRepositoryImpl,
) : ViewModel() {
    //States
    private val _isLoading = mutableStateOf(true)
    val isLoading by _isLoading

    private val _error = mutableStateOf<String?>(null)
    val error by _error

    private val _success = mutableStateOf(false)
    val success by _success

    private val _initScreen = mutableStateOf(true)
    val initScreen by _initScreen

    //Data
    private var _client = mutableStateOf<Client?>(null)
    val client by _client

    private var _phone = mutableStateOf<Phone?>(null)
    val phone by _phone

    //Controllers
    private val _receiptNumberText = mutableStateOf("")
    val receiptNumberText by _receiptNumberText

    private val _amountNumberText = mutableStateOf("")
    val amountNumberText by _amountNumberText

    private val _exchangeIndex= mutableIntStateOf(0)

    private val _paymentType = mutableStateOf("")
    val paymentType by _paymentType

    fun setReceiptNumberText(receiptNumber: String) {
        _receiptNumberText.value = receiptNumber
    }
    fun setAmountNumberText(amountNumber: String) {
        _amountNumberText.value = amountNumber
    }

    fun loadData(clientId: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _client.value = clientRepository.getClienteById(clientId)
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
                _success.value = true
                setPhone()
            }
        }
    }

    private fun setPhone(){
        val phone1 = _client.value!!.telefono1
        val phone2 = _client.value!!.telefono2
        val phoneSms = _client.value!!.telefonoSms
        val phones = listOf(
            Phone(phone1,"Teléfono principal",0),
            Phone(phone2,"Teléfono secundario", 0),
            Phone(phoneSms,"Teléfono SMS", 0)
        )
        phones.forEach{phone->
            if(phone.number.isNotEmpty()){
                _phone.value = phone
                return
            }
        }
    }

    fun updateExchangeIndex(index: Int) {
        _exchangeIndex.intValue = index
    }

    fun setPaymentType(type: String) {
        _paymentType.value = type
    }

    fun enableRegisterButton(): Boolean {

        return _receiptNumberText.value.isNotEmpty() && _paymentType.value.isNotEmpty() && _amountNumberText.value.isNotEmpty() && _phone.value != null
    }

    fun doRegister() {
        if (_initScreen.value && !enableRegisterButton()) {
            _initScreen.value = false
            return
        }
        val newRegister = createReceiptRegisterObject()
        Timber.d(newRegister.toString())
    }

    private fun createReceiptRegisterObject(): ReceiptRegister {
        val clienteId = _client.value!!.clienteId
        val vendedorId = 1
        val monedaId = _exchangeIndex.intValue
        val nDocumento = _receiptNumberText.value.toInt()
        val telefono = _phone.value!!.number
        val condicion = _paymentType.value
        val importe = _amountNumberText.value.toDouble()

        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        val fecha = currentDateTime.format(formatter)




        return ReceiptRegister(
            clienteId = clienteId,
            vendedorId = vendedorId,
            monedaId = monedaId,
            nDocumento = nDocumento,
            telefono = telefono,
            condicion = condicion,
            importe = importe,
            fecha = fecha
        )
    }
}