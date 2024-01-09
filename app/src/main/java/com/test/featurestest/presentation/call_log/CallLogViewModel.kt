package com.test.featurestest.presentation.call_log

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import android.net.Uri
import com.test.featurestest.data.repository.ClientRepositoryImpl
import com.test.featurestest.data.repository.ResultRepositoryImpl
import com.test.featurestest.domain.model.Client
import com.test.featurestest.domain.model.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import android.provider.CallLog
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewModelScope
import com.test.featurestest.domain.model.Phone
import com.test.featurestest.domain.model.CallRegister
import com.test.featurestest.util.Constants.MIN_DURATION
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@HiltViewModel
class CallLogViewModel @Inject constructor(
    private val clientRepository: ClientRepositoryImpl,
    private val resultRepository: ResultRepositoryImpl,
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

    private var _phones = mutableStateOf<List<Phone>>(emptyList())
    val phones by _phones

    private var _results = mutableStateOf<List<Result>>(emptyList())
    val results by _results


    private val _selectedResultIndex = mutableStateOf<Int?>(null)
    val selectedResultIndex by _selectedResultIndex

    private val _commentText = mutableStateOf("")
    val commentText by _commentText

    fun setCommentText(comment: String) {
        _commentText.value = comment
    }

    fun updateSelectedResultIndex(newIndex: Int) {
        _selectedResultIndex.value = newIndex
    }


    fun loadData(clientId: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _client.value = clientRepository.getClienteById(clientId)
                _results.value = resultRepository.getResults()
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
                _success.value = true
                setPhones()
            }
        }
    }

    private fun setPhones(){
        val phone1 = _client.value!!.telefono1
        val phone2 = _client.value!!.telefono2
        val phoneSms = _client.value!!.telefonoSms
        val phones = listOf(
            Phone(phone1,"Teléfono principal",0),
            Phone(phone2,"Teléfono secundario", 0),
            Phone(phoneSms,"Teléfono SMS", 0)
        )
        _phones.value = phones
    }
    fun updatePhones(context:Context){
        _phones.value = _phones.value.map { phone ->
            phone.copy(lastDuration = getLastCallDuration(context, phone.number))
        }
    }

    fun makePhoneCall(context: Context, phoneNumber: String) {
        val intent = Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse("tel:$phoneNumber")
        context.startActivity(intent)
    }

    @SuppressLint("Range")
    fun getLastCallDuration(context: Context, phoneNumber: String): Long {
        val cursor = context.contentResolver.query(
            CallLog.Calls.CONTENT_URI,
            arrayOf(CallLog.Calls.DURATION),
            "${CallLog.Calls.NUMBER} = ?",
            arrayOf(phoneNumber),
            "${CallLog.Calls.DATE} DESC"
        )
        var duration = 0L
        if (cursor?.moveToFirst() == true) {
            duration = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DURATION))
        }
        cursor?.close()
        return duration
    }

    private fun getDuration(): Phone? {
        _phones.value.forEach { phone ->
            if (phone.lastDuration >= MIN_DURATION) {
                return phone
            }
        }
        return null
    }

    fun getDurationCond(): Boolean {
        val phone = getDuration()
        return phone != null
    }

    fun enableRegisterButton(): Boolean {

        return _selectedResultIndex.value !=null && _commentText.value.isNotEmpty() && getDurationCond()
    }


    fun doRegister() {
        if (_initScreen.value && !enableRegisterButton()) {
            _initScreen.value = false
            return
        }
        val newRegister = createCallRegisterObject()
        Timber.d(newRegister.toString())
    }

    private fun createCallRegisterObject(): CallRegister {
        val rutaId = _client.value!!.rutaId
        val clienteId = _client.value!!.clienteId
        val vendedorId = 1
        val resultadoId = _results.value[_selectedResultIndex.value!!].id
        val resultado = _results.value[_selectedResultIndex.value!!].description
        val phone = getDuration()!!

        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        val fecha = currentDateTime.format(formatter)

        val comentario = _commentText.value


        return CallRegister(
            rutaId = rutaId,
            clienteId = clienteId,
            vendedorId = vendedorId,
            resultadoId = resultadoId,
            resultado = resultado,
            telefono = phone.number,
            duracion = phone.lastDuration,
            fecha = fecha,
            comentario = comentario
        )
    }
}