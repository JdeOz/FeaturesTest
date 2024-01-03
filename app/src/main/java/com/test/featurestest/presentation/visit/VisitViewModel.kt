package com.test.featurestest.presentation.visit

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.featurestest.data.repository.ClientRepositoryImpl
import com.test.featurestest.data.repository.ResultRepositoryImpl
import com.test.featurestest.domain.model.Client
import com.test.featurestest.domain.model.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
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

    private var _client = mutableStateOf<Client?>(null)
    val client by _client

    private var _results = mutableStateOf<List<Result>>(emptyList())
    val results by _results

    private val _initScreen = mutableStateOf(true)
    val initScreen by _initScreen

    private val _selectedDirectionIndex = mutableIntStateOf(0)

    private val _isSelectedDirection = mutableStateOf(false)
    val isSelectedDirection by _isSelectedDirection

    private val _indexSelectedResult = mutableIntStateOf(0)

    private val _isSelectedResult = mutableStateOf(false)
    val isSelectedResult by _isSelectedResult

    private val _showCameraDialog  = mutableStateOf(false)
    val showCameraDialog  by _showCameraDialog

    private val _showImageDialog  = mutableStateOf(false)
    val showImageDialog  by _showImageDialog

    private val _commentText = mutableStateOf("")
    val commentText by _commentText

    private val _imageUri =  mutableStateOf<List<Uri>>(emptyList())
    val imageUri by _imageUri


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

    fun updateDirectionSelectedIndex(newIndex: Int) {
        _selectedDirectionIndex.intValue = newIndex
        _isSelectedDirection.value = true
    }

    fun updateResultSelectedIndex(newIndex: Int) {
        _indexSelectedResult.intValue = newIndex
        _isSelectedResult.value = true
    }

    fun setShowCameraDialog(show: Boolean) {
        _showCameraDialog.value = show
    }

    fun addUriImage(uri: Uri){
        _imageUri.value = _imageUri.value + uri
    }
    fun popUriImage(uri: Uri){
        _imageUri.value = _imageUri.value.filter{ it != uri }
    }

    fun setCommentText(comment: String) {
        _commentText.value = comment
    }

    fun enableRegisterButton(): Boolean {
        return _isSelectedResult.value && _isSelectedDirection.value && _commentText.value.isNotEmpty()
    }

    fun doRegister() {
        if (_initScreen.value) {
            _initScreen.value = false
            return
        }
//        val newRegister = createRegisterObject()
//        Timber.d(newRegister.toString())
    }
}