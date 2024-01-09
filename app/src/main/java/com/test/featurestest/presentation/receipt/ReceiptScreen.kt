package com.test.featurestest.presentation.receipt

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.test.featurestest.presentation.components.ClientCard
import com.test.featurestest.presentation.components.MyDropDawnMenu
import com.test.featurestest.presentation.components.MyScaffold
import com.test.featurestest.presentation.components.ProgressIndicator
import com.test.featurestest.presentation.components.RadioButtonGroup
import com.test.featurestest.presentation.components.WarningText
import timber.log.Timber

@Composable
fun ReceiptScreen(
    navController: NavHostController,
    clientId: String,
    viewModel: ReceiptViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.loadData(clientId)
    }

    MyScaffold(
        navController = navController,
        title = "Registrar recibo de cobranza"

    ) { innerPadding ->
        when {
            viewModel.isLoading -> {
                ProgressIndicator(innerPadding)
            }

            viewModel.error != null -> {
                Timber.d(viewModel.error)
            }

            viewModel.success -> {
                ReceiptContent(innerPadding, viewModel)
            }
        }
    }
}

@Composable
fun ReceiptContent(
    innerPadding: PaddingValues,
    viewModel: ReceiptViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .verticalScroll(rememberScrollState())
    ) {
        ClientCard(viewModel.client!!.dCliente)

        Divider()

        PhoneNumber(viewModel)

        ReceiptNumber(viewModel)

        Spacer(modifier = Modifier.height(8.dp))

        PaymentType(viewModel)

        AmountField(viewModel)

        Spacer(modifier = Modifier.weight(1f))

        RegisterButton(viewModel)
    }
}

@Composable
fun PhoneNumber(viewModel: ReceiptViewModel) {
    OutlinedTextField(
        value = viewModel.phone?.number ?: "Número no encontrado",
        onValueChange = {},
        isError = viewModel.phone == null,
        label = { Text(viewModel.phone?.name ?: "Número no encontrado") },
        singleLine = true,
        readOnly = true,
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .imePadding()
            .padding(4.dp)
    )
}

@Composable
fun ReceiptNumber(viewModel: ReceiptViewModel) {
    OutlinedTextField(
        value = viewModel.receiptNumberText,
        onValueChange = { viewModel.setReceiptNumberText(it) },
        isError = viewModel.receiptNumberText.isEmpty() && !viewModel.initScreen,
        label = { Text("Nro. de Recibo") },
        placeholder = { Text(text = "Ingrese el número de Recibo") },
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
fun PaymentType(viewModel: ReceiptViewModel) {
    Text(
        text = "Condición",
        modifier = Modifier.padding(4.dp),
        fontWeight = FontWeight.Bold
    )
    SelectPaymentType(viewModel)
}

@Composable
fun SelectPaymentType(viewModel:ReceiptViewModel) {
    val animalTypes = listOf("Crédito", "Contado")

    RadioButtonGroup(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth(),
        items = animalTypes,
        selection = viewModel.paymentType,
        onItemClick = { clickedItem ->
            viewModel.setPaymentType(clickedItem)
        }
    )
}

@Composable
fun AmountField(viewModel: ReceiptViewModel) {
    Row {
        MyDropDawnMenu(
            selectionOptions = listOf("S/.", "$"),
            init = viewModel.initScreen,
            label = "",
            placeholder = "",
            preSelected = true,
            modifier = Modifier.weight(1f),
            onSelectionChange = { index ->
                viewModel.updateExchangeIndex(index)
            }
        )
        AmountNumber(viewModel, Modifier.weight(3f))
    }
}

@Composable
fun AmountNumber(viewModel: ReceiptViewModel, modifier: Modifier) {
    OutlinedTextField(
        value = viewModel.amountNumberText,
        onValueChange = { viewModel.setAmountNumberText(it) },
        isError = viewModel.amountNumberText.isEmpty() && !viewModel.initScreen,
        label = { Text("Importe") },
        placeholder = { Text(text = "Ingrese el Importe") },
        singleLine = true,
        shape = RoundedCornerShape(4.dp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = modifier
            .fillMaxWidth()
            .imePadding()
            .padding(4.dp)
    )
}

@Composable
fun RegisterButton(viewModel: ReceiptViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (viewModel.phone == null) {
            WarningText("El ciente no tiene teléfono válido.")
        }
        if (!viewModel.initScreen) {
            if (viewModel.receiptNumberText.isEmpty()) {
                WarningText("Escriba el Nro. de recibo.")
            }
            if (viewModel.paymentType.isEmpty()) {
                WarningText("Seleccione una tipo de pago.")
            }
            if (viewModel.amountNumberText.isEmpty()) {
                WarningText("Escriba el importe.")
            }
        }

        Button(
            onClick = {
                viewModel.doRegister()
            },
            shape = RoundedCornerShape(4.dp),
            enabled = viewModel.phone != null && (viewModel.enableRegisterButton() || viewModel.initScreen)
        ) {
            Icon(Icons.Default.Send, contentDescription = "REGISTRAR")
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "REGISTRAR")
        }
    }
}


