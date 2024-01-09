package com.test.featurestest.presentation.call_log

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.test.featurestest.domain.model.Phone
import com.test.featurestest.presentation.components.ClientCard
import com.test.featurestest.presentation.components.MyDropDawnMenu
import com.test.featurestest.presentation.components.MyScaffold
import com.test.featurestest.presentation.components.ProgressIndicator
import com.test.featurestest.presentation.components.WarningText
import com.test.featurestest.presentation.components.WithPermissions
import com.test.featurestest.util.Constants.MIN_DURATION
import timber.log.Timber

@Composable
fun CallLogScreen(
    navController: NavHostController,
    clientId: String,
    viewModel: CallLogViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.loadData(clientId)
    }

    MyScaffold(
        navController = navController,
        title = "Registrar Llamada"

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
                        android.Manifest.permission.READ_CALL_LOG,
                        android.Manifest.permission.CALL_PHONE
                    ),
                    onPermissionsGranted = { viewModel.updatePhones(context) }
                )
                CallLogContent(innerPadding, context, viewModel)
            }
        }
    }
}

@Composable
fun CallLogContent(innerPadding: PaddingValues, context: Context, viewModel: CallLogViewModel) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .verticalScroll(rememberScrollState())
    ) {
        ClientCard(viewModel.client!!.dCliente)

        Divider()

        PhoneCards(viewModel, context)

        Spacer(modifier = Modifier.height(8.dp))

        UpdateDurations(viewModel, context)

        SelectResult(viewModel)

        Comment(viewModel)

        Spacer(modifier = Modifier.weight(1f))

        RegisterButton(viewModel)
    }
}

@Composable
fun PhoneCards(viewModel: CallLogViewModel, context: Context) {
    viewModel.phones.forEach { phone ->
        PhoneCard(viewModel = viewModel, context = context, phone = phone)
    }
}

@Composable
fun PhoneCard(
    viewModel: CallLogViewModel,
    context: Context,
    phone: Phone
) {

    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .clickable(onClick = {
                        viewModel.makePhoneCall(context, phone.number)
                    })
                    .size(32.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(MaterialTheme.colorScheme.secondary)
                    .padding(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = "Icono de teléfono"
                )
            }

            Spacer(modifier = Modifier.width(8.dp))
            Text("${phone.number} (${phone.name})")
        }
        Text(
            text = phone.lastDuration.toString() + " Seg.",
            color = if (!viewModel.getDurationCond() && !viewModel.initScreen) MaterialTheme.colorScheme.error else Color.Unspecified
        )
    }
}

@Composable
fun UpdateDurations(viewModel: CallLogViewModel, context: Context) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Box(
            modifier = Modifier
                .clickable(onClick = {
                    viewModel.updatePhones(context)
                })
                .size(32.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(MaterialTheme.colorScheme.secondary)
                .padding(4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "Actualizar",
            )
        }

        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Actualizar Registro",
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun SelectResult(viewModel: CallLogViewModel) {
    MyDropDawnMenu(
        selectionOptions = viewModel.results.map { it.description },
        init = viewModel.initScreen,
        label = "Resultado",
        placeholder = "Seleccione un resultado",
        onSelectionChange = { index ->
            viewModel.updateSelectedResultIndex(index)
        }
    )
}

@Composable
fun Comment(viewModel: CallLogViewModel) {
    OutlinedTextField(
        value = viewModel.commentText,
        onValueChange = { viewModel.setCommentText(it) },
        isError = viewModel.commentText.isEmpty() && !viewModel.initScreen,
        label = { Text("Comentarios") },
        placeholder = { Text(text = "Ingrese los comentarios") },
        maxLines = 3,
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .imePadding()
            .padding(4.dp)
    )
}

@Composable
fun RegisterButton(viewModel: CallLogViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!viewModel.initScreen) {
            if (!viewModel.getDurationCond()) {
                WarningText("Alguna llamada debe durar al menos $MIN_DURATION s.")
            }
            if (viewModel.selectedResultIndex == null) {
                WarningText("Seleccione un resultado.")
            }
            if (viewModel.commentText.isEmpty()) {
                WarningText("Escriba los comentarios.")
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