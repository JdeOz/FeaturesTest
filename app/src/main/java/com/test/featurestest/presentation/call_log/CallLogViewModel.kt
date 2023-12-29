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
import com.test.featurestest.data.repository.ClientRepositoryImpl
import com.test.featurestest.data.repository.ResultRepositoryImpl
import com.test.featurestest.domain.model.Client
import com.test.featurestest.domain.model.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import android.provider.CallLog
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.lifecycle.viewModelScope
import com.test.featurestest.util.Constants.MIN_DURATION
import kotlinx.coroutines.launch

@HiltViewModel
class CallLogViewModel @Inject constructor(
    private val clientRepository: ClientRepositoryImpl,
    private val resultRepository: ResultRepositoryImpl,
) : ViewModel() {

    private val _client = MutableLiveData<Client>()
    val client: LiveData<Client> = _client

    private var _isExpanded = mutableStateOf(false)
    val isExpanded: State<Boolean> = _isExpanded

    private var _results = MutableLiveData<List<Result>>()
    val results: LiveData<List<Result>> = _results

    private val _result = mutableStateOf("")
    val result: State<String> = _result

    private val _commentText = mutableStateOf("")
    val commentText: State<String> = _commentText

    private val _initScreen = mutableStateOf(true)
    val initScreen: State<Boolean> = _initScreen

    private val _callDurations = mutableStateOf<List<Long>>(listOf(0L, 0L, 0L))
    val callDurations: State<List<Long>> = _callDurations

    fun setCommentText(comment: String) {
        _commentText.value = comment
    }

    fun setExpanded(value: Boolean) {
        _isExpanded.value = value
    }

    fun selectGender(newGender: String) {
        _result.value = newGender
        _isExpanded.value = false
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


    fun updateDurations(context: Context) {
        val updatedDurations = listOf(
            getLastCallDuration(context, _client.value!!.telefono1),
            getLastCallDuration(context, _client.value!!.telefono2),
            getLastCallDuration(context, _client.value!!.telefonoSms)
        )

        _callDurations.value = updatedDurations
    }

    fun getDurationCond():Boolean{
        var durationCond = false
        _callDurations.value.forEach { duration ->
            if(duration>= MIN_DURATION){
                durationCond = true
            }
        }
        return durationCond
    }

    fun enableRegisterButton(): Boolean {

        return _result.value.isNotEmpty() && _commentText.value.isNotEmpty() && getDurationCond()
    }

    fun doRegister() {
        if (_initScreen.value) {
            _initScreen.value = false
        }
    }
}