package com.test.featurestest.presentation.visit

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.test.featurestest.data.repository.ClientRepositoryImpl
import com.test.featurestest.data.repository.ResultRepositoryImpl
import com.test.featurestest.domain.model.Client
import com.test.featurestest.domain.model.Result
import com.test.featurestest.domain.model.VisitRegister
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class VisitViewModel @Inject constructor(
    private val clientRepository: ClientRepositoryImpl,
    private val resultRepository: ResultRepositoryImpl,
) : ViewModel() {
    private val _isLoading = mutableStateOf(true)
    val isLoading by _isLoading

    private val _error = mutableStateOf<String?>(null)
    val error by _error

    private val _success = mutableStateOf(false)
    val success by _success

    private var _client = mutableStateOf<Client?>(null)
    val client by _client

    private var _results = mutableStateOf<List<Result>>(emptyList())
    val results by _results

    private val _initScreen = mutableStateOf(true)
    val initScreen by _initScreen

    private val _selectedDirectionIndex = mutableStateOf<Int?>(null)
    val selectedDirectionIndex by _selectedDirectionIndex

    private val _selectedResultIndex = mutableStateOf<Int?>(null)
    val selectedResultIndex by _selectedResultIndex

    private val _showCameraDialog = mutableStateOf(false)
    val showCameraDialog by _showCameraDialog

    private val _showMapDialog = mutableStateOf(false)
    val showMapDialog by _showMapDialog

    private val _commentText = mutableStateOf("")
    val commentText by _commentText

    private val _imageUris = mutableStateOf<List<Uri>>(emptyList())
    val imageUris by _imageUris


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
            }
        }
    }

    fun updateDirectionSelectedIndex(newIndex: Int) {
        _selectedDirectionIndex.value = newIndex
    }

    fun updateResultSelectedIndex(newIndex: Int) {
        _selectedResultIndex.value = newIndex
    }

    fun setShowCameraDialog(show: Boolean) {
        _showCameraDialog.value = show
    }

    fun setShowMapDialog(show: Boolean) {
        _showMapDialog.value = show
    }

    fun addUriImage(uri: Uri) {
        _imageUris.value = _imageUris.value + uri
    }

    fun popUriImage(uri: Uri) {
        _imageUris.value = _imageUris.value.filter { it != uri }
    }

    fun setCommentText(comment: String) {
        _commentText.value = comment
    }

    fun enableRegisterButton(): Boolean {
        return _selectedResultIndex.value!=null && _selectedDirectionIndex.value!=null && _commentText.value.isNotEmpty() && _imageUris.value.isNotEmpty()
    }

    fun getDirectionCoords(): LatLng {
        val lat = client!!.direcciones[_selectedDirectionIndex.value!!].latitud.toDouble()
        val long = client!!.direcciones[_selectedDirectionIndex.value!!].longitud.toDouble()
        return LatLng(lat, long)
    }

    fun doRegister() {
        if (_initScreen.value && !enableRegisterButton()) {
            _initScreen.value = false
            return
        }
        val newVisitRegister = createVisitRegisterObject()
        Timber.d(newVisitRegister.toString())
    }

    private fun createVisitRegisterObject(): VisitRegister {
        val clienteId = _client.value!!.clienteId
        val vendedorId = 1
        val rutaId = _client.value!!.rutaId
        val clienteAlmacenId = _client.value!!.direcciones[_selectedDirectionIndex.value!!].personaAlmacenId
        val resultadoId = _results.value[_selectedResultIndex.value!!].id
        val resultado = _results.value[_selectedResultIndex.value!!].description

        val foto1 = _imageUris.value[0]
        val foto2 = if (_imageUris.value.size > 1) _imageUris.value[1] else null

//        val currentDateTime = LocalDateTime.now()
//        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
//        val fecha = currentDateTime.format(formatter)

        val comentario = _commentText.value
        val latitud = _client.value!!.direcciones[_selectedDirectionIndex.value!!].latitud
        val longitud = _client.value!!.direcciones[_selectedDirectionIndex.value!!].longitud


        return VisitRegister(
            clienteId = clienteId,
            vendedorId = vendedorId,
            rutaId = rutaId,
            clienteAlmacenId=clienteAlmacenId,
            resultadoContactoId = resultadoId,
            resultado = resultado,
            foto1Uri = foto1,
            foto2Uri = foto2,
            comentario = comentario,
            latitud = latitud,
            longitud = longitud
        )
    }

}