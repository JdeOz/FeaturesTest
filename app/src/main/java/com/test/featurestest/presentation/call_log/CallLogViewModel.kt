package com.test.featurestest.presentation.call_log

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.net.Uri
import android.os.Build
import com.test.featurestest.data.repository.ClientRepositoryImpl
import com.test.featurestest.data.repository.ResultRepositoryImpl
import com.test.featurestest.domain.model.Client
import com.test.featurestest.domain.model.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import android.provider.CallLog
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.annotation.RequiresApi
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

    private val _client = MutableLiveData<Client>()
    val client: LiveData<Client> = _client

    private var _phones = MutableLiveData<List<Phone>>()
    val phones: LiveData<List<Phone>> = _phones

    private var _isExpanded = mutableStateOf(false)
    val isExpanded: State<Boolean> = _isExpanded

    private var _results = MutableLiveData<List<Result>>()
    val results: LiveData<List<Result>> = _results

    private val _result = MutableLiveData<Result>()
    val result: LiveData<Result> = _result

    private val _selectedResult = mutableStateOf(false)
    val selectedResult: State<Boolean> = _selectedResult

    private val _commentText = mutableStateOf("")
    val commentText: State<String> = _commentText

    private val _initScreen = mutableStateOf(true)
    val initScreen: State<Boolean> = _initScreen

    fun setCommentText(comment: String) {
        _commentText.value = comment
    }

    fun setExpanded(value: Boolean) {
        _isExpanded.value = value
    }

    fun selectResult(newResult: Result) {
        _result.value = newResult
        _selectedResult.value = true
        _isExpanded.value = false
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

    fun loadClient(clientId: String, launcher: ManagedActivityResultLauncher<String, Boolean>) {
        viewModelScope.launch {
            _client.value = clientRepository.getClienteById(clientId)
            launcher.launch(android.Manifest.permission.READ_CALL_LOG)
        }

    }

    fun loadResult() {
        viewModelScope.launch {
            _results.value = resultRepository.getResults()
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

    fun getDuration(): Phone? {
        _phones.value!!.forEach { phone ->
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

        return _selectedResult.value && _commentText.value.isNotEmpty() && getDurationCond()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun doRegister() {
        if (_initScreen.value) {
            _initScreen.value = false
            return
        }
        val newRegister = createRegisterObject()
        Timber.d(newRegister.toString())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createRegisterObject(): Register {
        val rutaId = _client.value!!.rutaId
        val clienteId = _client.value!!.clienteId
        val vendedorId = 1
        val resultadoId = _result.value!!.id
        val resultado = _result.value!!.description
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