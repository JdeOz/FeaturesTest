package com.test.featurestest.presentation.call_log

import android.content.Context
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.test.featurestest.domain.model.Phone
import com.test.featurestest.util.Constants.MIN_DURATION


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CallLogScreen(
    navController: NavHostController,
    clientId: String,
    viewModel: CallLogViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                viewModel.updatePhones(context)
            }
        })

    LaunchedEffect(Unit) {
        viewModel.loadClient(clientId, launcher)
        viewModel.loadResult()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.secondary,
                    navigationIconContentColor = MaterialTheme.colorScheme.secondary
                ),
                title = { Text("Registrar Llamada") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        if (viewModel.client.observeAsState().value != null && viewModel.results.observeAsState().value != null) {
            CallLogContent(innerPadding, context, viewModel)
        }
    }
}

@Composable
fun CallLogContent(innerPadding: PaddingValues, context: Context, viewModel: CallLogViewModel) {


    val client = viewModel.client.observeAsState().value!!
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .verticalScroll(scrollState)
    ) {
        ClientName(client.dCliente)

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
fun ClientName(clientName: String) {
    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "Icono de persona",

            )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = clientName,
            color = Color.Gray,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun PhoneCard(
    viewModel: CallLogViewModel,
    context: Context,
    phone: Phone
) {

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                viewModel.makePhoneCall(context, phone.number)
            }
        })

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
                        launcher.launch(android.Manifest.permission.CALL_PHONE)
                    })
                    .size(32.dp)
                    .background(Color.LightGray, RoundedCornerShape(4.dp))
                    .padding(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = "Icono de teléfono"
                )
            }

            Spacer(modifier = Modifier.width(8.dp))
            Text("${phone.number} (${phone.name})", color = Color.Gray)
        }
        Text(
            text = phone.lastDuration.toString() + " Seg.",
            color = if (!viewModel.getDurationCond() && !viewModel.initScreen.value) MaterialTheme.colorScheme.error else Color.Gray
        )
    }
}

@Composable
fun PhoneCards(viewModel: CallLogViewModel, context: Context) {

    viewModel.phones.observeAsState().value?.forEach() { phone ->
        PhoneCard(viewModel = viewModel, context = context, phone = phone)
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
                .background(Color.LightGray, RoundedCornerShape(4.dp))
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
            fontWeight = FontWeight.Bold,
            color = Color.Gray,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectResult(viewModel: CallLogViewModel) {
    ExposedDropdownMenuBox(
        expanded = viewModel.isExpanded.value,
        onExpandedChange = { viewModel.setExpanded(it) },
        modifier = Modifier.padding(4.dp)
    ) {
        OutlinedTextField(
            value = if(viewModel.selectedResult.value)viewModel.result.value!!.description else "",
            onValueChange = {},
            readOnly = true,
            isError = !viewModel.selectedResult.value && !viewModel.initScreen.value,
            label = { Text(text = "Resultado") },
            placeholder = { Text(text = "Seleccione un resultado") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = viewModel.isExpanded.value)
            },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )
        DropdownMenu(
            expanded = viewModel.isExpanded.value,
            onDismissRequest = { viewModel.setExpanded(false) },
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .exposedDropdownSize()
        ) {
            viewModel.results.observeAsState().value?.forEach { selectionOption ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = selectionOption.description,
                            color = Color.Gray
                        )
                    },
                    onClick = {
                        viewModel.selectResult(selectionOption)
                        viewModel.setExpanded(false)
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}

@Composable
fun Comment(viewModel: CallLogViewModel) {
    OutlinedTextField(
        value = viewModel.commentText.value,
        onValueChange = { viewModel.setCommentText(it) },
        isError = viewModel.commentText.value.isEmpty() && !viewModel.initScreen.value,
        label = { Text("Comentarios") },
        placeholder = { Text(text = "Ingrese los comentarios") },
        maxLines = 3,
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    )
}

@Composable
fun WarningText(warning: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Outlined.Info,
            tint = MaterialTheme.colorScheme.error,
            contentDescription = "Warning"
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = warning,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(bottom = 4.dp)
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RegisterButton(viewModel: CallLogViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
//            .wrapContentSize(Alignment.Center)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!viewModel.initScreen.value) {
            if (!viewModel.getDurationCond()) {
                WarningText("Alguna llamada debe durar al menos $MIN_DURATION s.")
            }
            if (!viewModel.selectedResult.value) {
                WarningText("Seleccione un resultado.")
            }
            if (viewModel.commentText.value.isEmpty()) {
                WarningText("Escriba los comentarios.")
            }
        }

        Button(
            onClick = {
                viewModel.doRegister()
            },
            shape = RoundedCornerShape(4.dp),
            enabled = viewModel.enableRegisterButton() || viewModel.initScreen.value
        ) {
            Icon(Icons.Default.Send, contentDescription = "REGISTRAR")
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "REGISTRAR")
        }
    }
}