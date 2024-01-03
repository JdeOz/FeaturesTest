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
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.viewModelScope
import com.test.featurestest.domain.model.Phone
import com.test.featurestest.domain.model.Register
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

    private val _isLoading = mutableStateOf(true)
    val isLoading by _isLoading

    private val _error = mutableStateOf<String?>(null)
    val error by _error

    private var _client = mutableStateOf<Client?>(null)
    val client by _client

    private var _phones = mutableStateOf<List<Phone>>(emptyList())
    val phones by _phones

    private var _results = mutableStateOf<List<Result>>(emptyList())
    val results by _results

    private val _initScreen = mutableStateOf(true)
    val initScreen by _initScreen

    private val _selectedResultIndex = mutableIntStateOf(0)

    private val _isSelectedResult = mutableStateOf(false)
    val isSelectedResult by _isSelectedResult

    private val _commentText = mutableStateOf("")
    val commentText by _commentText

    fun setCommentText(comment: String) {
        _commentText.value = comment
    }

    fun updateSelectedResultIndex(newIndex: Int) {
        _selectedResultIndex.intValue = newIndex
        _isSelectedResult.value = true
    }

    fun updatePhones(context:Context){
        val phone1 = _client.value!!.telefono1
        val phone2 = _client.value!!.telefono2
        val phoneSms = _client.value!!.telefonoSms
        val phones = listOf(
            Phone(phone1,"Teléfono principal", getLastCallDuration(context, phone1)),
            Phone(phone2,"Teléfono secundario", getLastCallDuration(context, phone2)),
            Phone(phoneSms,"Teléfono SMS", getLastCallDuration(context,phoneSms))
        )
        _phones.value = phones
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
            }
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

        return _isSelectedResult.value && _commentText.value.isNotEmpty() && getDurationCond()
    }


    fun doRegister() {
        if (_initScreen.value) {
            _initScreen.value = false
            return
        }
        val newRegister = createRegisterObject()
        Timber.d(newRegister.toString())
    }

    private fun createRegisterObject(): Register {
        val rutaId = _client.value!!.rutaId
        val clienteId = _client.value!!.clienteId
        val vendedorId = 1
        val resultadoId = _results.value[_selectedResultIndex.intValue].id
        val resultado = _results.value[_selectedResultIndex.intValue].description
        val phone = getDuration()!!

        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        val fecha = currentDateTime.format(formatter)

        val comentario = _commentText.value


        return Register(
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