package com.test.featurestest.presentation.call_log

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.test.featurestest.domain.model.Client

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CallLogScreen(navController: NavHostController,clientId: String) {
    val viewModel = CallLogViewModel()
    val client = viewModel.client.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.loadCliente(clientId)
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
        client.value?.let { client ->
            CallLogContent(innerPadding, client)
        } ?: Text("Cargando datos del cliente...")
    }
}

@Composable
fun CallLogContent(innerPadding: PaddingValues, client: Client) {
    Column(modifier = Modifier.padding(innerPadding)) {
        ClientName(client.dCliente)

        Divider()

        PhoneCard(phoneNumber = client.telefono1, phoneType = "Teléfono principal")
        PhoneCard(phoneNumber = client.telefono2, phoneType = "Teléfono secundario")
        PhoneCard(phoneNumber = client.telefonoSms, phoneType = "Teléfono SMS")

        Spacer(modifier = Modifier.height(8.dp))

        UpdateRegister()
    }
}


@Composable
fun ClientName(clientName: String) {
    Row(modifier = Modifier
        .padding(16.dp)
        .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,) {
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
fun PhoneCard(phoneNumber: String, phoneType: String) {

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
            Text("$phoneNumber ($phoneType)",color = Color.Gray)
        }
        Text(text = "0 Seg.",color = Color.Gray)
    }
}

@Composable
fun UpdateRegister() {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Box(
            modifier = Modifier
                .clickable(onClick = {
                    // acción
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
