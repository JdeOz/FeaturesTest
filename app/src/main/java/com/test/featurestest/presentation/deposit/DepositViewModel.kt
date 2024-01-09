package com.test.featurestest.presentation.deposit

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.featurestest.data.repository.BankAccountRepositoryImpl
import com.test.featurestest.data.repository.ClientRepositoryImpl
import com.test.featurestest.data.repository.OfficeRepositoryImpl
import com.test.featurestest.data.repository.ReceiptRepositoryImpl
import com.test.featurestest.domain.model.BankAccount
import com.test.featurestest.domain.model.Client
import com.test.featurestest.domain.model.Office
import com.test.featurestest.domain.model.ReceiptRegister
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DepositViewModel @Inject constructor(
    private val clientRepository: ClientRepositoryImpl,
    private val officeRepository: OfficeRepositoryImpl,
    private val bankAccountRepository: BankAccountRepositoryImpl,
    private val receiptRepository: ReceiptRepositoryImpl,
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

    private var _offices = mutableStateOf<List<Office>>(emptyList())
    val offices by _offices

    private var _bankAccounts = mutableStateOf<List<BankAccount>>(emptyList())
    val bankAccounts by _bankAccounts

    private var _receipts = mutableStateOf<List<ReceiptRegister>>(emptyList())
    val receipts by _receipts

    private val _selectedOfficeIndex = mutableStateOf<Int?>(null)
    val selectedOfficeIndex by _selectedOfficeIndex

    private val _optionBankIndex = mutableStateOf<Int?>(null)
    val optionBankIndex by _optionBankIndex

    private val _optionReceiptIndex = mutableStateOf<Int?>(null)
    val optionReceiptIndex by _optionReceiptIndex

    private val _operationNumberText = mutableStateOf("")
    val operationNumberText by _operationNumberText

    private val _exchangeIndex = mutableIntStateOf(0)

    private val _amountNumberText = mutableStateOf("")
    val amountNumberText by _amountNumberText

    private val _dateText = mutableStateOf("")
    val dateText by _dateText



    private val _showCameraDialog = mutableStateOf(false)
    val showCameraDialog by _showCameraDialog

    private val _imageUris = mutableStateOf<List<Uri>>(emptyList())
    val imageUris by _imageUris


    fun loadData(clientId: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _client.value = clientRepository.getClienteById(clientId)
                _offices.value = officeRepository.getOffices()
                _bankAccounts.value = bankAccountRepository.getBankAccounts()
                _receipts.value = receiptRepository.getReceipts()
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
                _success.value = true
            }
        }
    }

    fun updateSelectedOfficeIndex(newIndex: Int) {
        _selectedOfficeIndex.value = newIndex
    }

    fun updateOptionBankIndex(newIndex: Int) {
        _optionBankIndex.value = newIndex
    }

    fun updateOptionReceiptIndex(newIndex: Int) {
        _optionReceiptIndex.value = newIndex
    }

    fun setOperationNumberText(operationNumber: String) {
        _operationNumberText.value = operationNumber
    }

    fun updateExchangeIndex(index: Int) {
        _exchangeIndex.intValue = index
    }

    fun setAmountNumberText(amountNumber: String) {
        _amountNumberText.value = amountNumber
    }

    fun setDateText(dateText: String) {
        _dateText.value = dateText
    }

    fun setShowCameraDialog(show: Boolean) {
        _showCameraDialog.value = show
    }

    fun addUriImage(uri: Uri) {
        _imageUris.value = _imageUris.value + uri
    }

    fun getExchange(): String {
        return if (optionBankIndex == null) {
            "S/"
        } else {
            bankAccounts[optionBankIndex!!].moneda

        }

    }

    fun popUriImage(uri: Uri) {
        _imageUris.value = _imageUris.value.filter { it != uri }
    }

    fun enableRegisterButton(): Boolean {
        return _selectedOfficeIndex.value != null && _dateText.value.isNotEmpty() && _operationNumberText.value.isNotEmpty() && _amountNumberText.value.isNotEmpty() && _imageUris.value.isNotEmpty()
    }

    fun doRegister() {
        if (_initScreen.value && !enableRegisterButton()) {
            _initScreen.value = false
            return
        }
    }
}