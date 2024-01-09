package com.test.featurestest.presentation.deposit

import android.Manifest
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.test.featurestest.domain.model.BankAccount
import com.test.featurestest.domain.model.ReceiptRegister
import com.test.featurestest.presentation.components.ClientCard
import com.test.featurestest.presentation.components.DateField
import com.test.featurestest.presentation.components.FullScreenCameraDialog
import com.test.featurestest.presentation.components.ImageCards
import com.test.featurestest.presentation.components.MyDropDawnMenu
import com.test.featurestest.presentation.components.MyScaffold
import com.test.featurestest.presentation.components.OptionField
import com.test.featurestest.presentation.components.ProgressIndicator
import com.test.featurestest.presentation.components.WarningText
import com.test.featurestest.presentation.components.WithPermissions
import timber.log.Timber

@Composable
fun DepositScreen(
    navController: NavHostController,
    clientId: String,
    viewModel: DepositViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.loadData(clientId)
    }

    MyScaffold(
        navController = navController,
        title = "Registrar depósito de cobranza",
    ) { innerPadding ->
        when {
            viewModel.isLoading -> {
                ProgressIndicator(innerPadding)
            }

            viewModel.error != null -> {
                Timber.d(viewModel.error)
            }

            viewModel.success -> {
                WithPermissions(
                    permissions = listOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                )
                DepositContent(innerPadding, viewModel)
            }
        }
    }
}

@Composable
fun DepositContent(
    innerPadding: PaddingValues,
    viewModel: DepositViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .verticalScroll(rememberScrollState())
    ) {
        ClientCard(viewModel.client!!.dCliente)

        Divider()

        BankAccountsField(viewModel)

        ReceiptsField(viewModel)

        OperationDate(viewModel)

        SelectOffice(viewModel)

        OperationNumber(viewModel)

        AmountNumber(viewModel)

        CameraSection(viewModel)

        Spacer(modifier = Modifier.weight(1f))

        RegisterButton(viewModel)
    }
}

@Composable
fun BankAccountsField(viewModel: DepositViewModel) {
    OptionField(
        optionIndex = viewModel.optionBankIndex,
        init = viewModel.initScreen,
        title = "Banco / cuenta",
        selectionOptions = bankAccountString(viewModel.bankAccounts),
        onSelectedOption = {viewModel.updateOptionBankIndex(it)}
    )
}

@Composable
fun bankAccountString(bankAccounts: List<BankAccount>): List<String> {
    return bankAccounts.map { "${it.banco} | ${it.moneda} | ${it.numeroCuenta}" }
}


@Composable
fun ReceiptsField(viewModel: DepositViewModel) {
    OptionField(
        optionIndex = viewModel.optionReceiptIndex,
        init = viewModel.initScreen,
        title = "Nro Recibo",
        selectionOptions = receiptString(viewModel.receipts),
        onSelectedOption = {viewModel.updateOptionReceiptIndex(it)}
    )
}

@Composable
fun receiptString(bankAccounts: List<ReceiptRegister>): List<String> {
    return bankAccounts.map { "${it.nDocumento} | ${it.importe} | ${it.fecha}" }
}

@Composable
fun OperationDate(viewModel: DepositViewModel) {
    DateField(
        dateText = viewModel.dateText,
        init = viewModel.initScreen,
        onDatePicked = {viewModel.setDateText(it)}
    )
}


@Composable
fun SelectOffice(viewModel: DepositViewModel) {
    MyDropDawnMenu(
        selectionOptions = viewModel.offices.map { it.oficina },
        init = viewModel.initScreen,
        label = "Oficina",
        placeholder = "Seleccione una oficina",
        onSelectionChange = { index ->
            viewModel.updateSelectedOfficeIndex(index)
        }
    )
}

@Composable
fun OperationNumber(viewModel: DepositViewModel) {
    OutlinedTextField(
        value = viewModel.operationNumberText,
        onValueChange = { viewModel.setOperationNumberText(it) },
        isError = viewModel.operationNumberText.isEmpty() && !viewModel.initScreen,
        label = { Text("Nro. de Operación") },
        placeholder = { Text(text = "Ingrese el número de Operación") },
        singleLine = true,
        shape = RoundedCornerShape(4.dp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier
            .fillMaxWidth()
            .imePadding()
            .padding(4.dp)
    )
}

@Composable
fun AmountNumber(viewModel: DepositViewModel) {
    OutlinedTextField(
        value = viewModel.amountNumberText,
        onValueChange = { viewModel.setAmountNumberText(it) },
        isError = viewModel.amountNumberText.isEmpty() && !viewModel.initScreen,
        label = { Text("Importe") },
        placeholder = { Text(text = "Ingrese el Importe") },
        singleLine = true,
        prefix = {Text(viewModel.getExchange())},
        shape = RoundedCornerShape(4.dp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier
            .fillMaxWidth()
            .imePadding()
            .padding(4.dp)
    )
}

@Composable
fun CameraSection(viewModel: DepositViewModel) {
    FullScreenCameraDialog(
        viewModel.showCameraDialog,
        notShow = { viewModel.setShowCameraDialog(false) },
    )
    { viewModel.addUriImage(it) }

    ImageCards(
        viewModel.imageUris,
        popImage = { viewModel.popUriImage(it) },
        show = { viewModel.setShowCameraDialog(true) }
    )
}

@Composable
fun RegisterButton(viewModel: DepositViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!viewModel.initScreen) {
            if (viewModel.optionBankIndex == null) {
                WarningText("Seleccione un Banco.")
            }
            if (viewModel.optionReceiptIndex == null) {
                WarningText("Seleccione un recibo.")
            }
            if (viewModel.dateText.isEmpty()) {
                WarningText("Seleccione una fecha.")
            }
            if (viewModel.selectedOfficeIndex == null) {
                WarningText("Seleccione un oficina.")
            }
            if (viewModel.operationNumberText.isEmpty()) {
                WarningText("Escriba el número de operación")
            }
            if (viewModel.amountNumberText.isEmpty()) {
                WarningText("Escriba el importe.")
            }
            if (viewModel.imageUris.isEmpty()) {
                WarningText("Adjunte al menos una fotos.")
            }
        }

        Button(
            onClick = {
                viewModel.doRegister()
            },
            shape = RoundedCornerShape(4.dp),
            enabled = viewModel.enableRegisterButton() || viewModel.initScreen
        ) {
            Icon(Icons.Default.Send, contentDescription = "REGISTRAR")
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "REGISTRAR")
        }
    }
}