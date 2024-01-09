package com.test.featurestest.presentation.visit


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.test.featurestest.domain.model.Client
import com.test.featurestest.presentation.components.ClientCard
import com.test.featurestest.presentation.components.FullScreenCameraDialog
import com.test.featurestest.presentation.components.ImageCards
import com.test.featurestest.presentation.components.MyAlertDialog
import com.test.featurestest.presentation.components.MyDropDawnMenu
import com.test.featurestest.presentation.components.MyScaffold
import com.test.featurestest.presentation.components.WarningText
import com.test.featurestest.presentation.components.WithPermissions
import com.test.featurestest.presentation.components.MyMapComposable
import com.test.featurestest.presentation.components.ProgressIndicator
import timber.log.Timber

@Composable
fun VisitScreen(
    navController: NavHostController,
    clientId: String,
    viewModel: VisitViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.loadData(clientId)
    }

    MyScaffold(
        navController = navController,
        title = "Registrar Visita",
        floatingActionButton = { MapButton(viewModel) }
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
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.CAMERA,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                )
                VisitContent(innerPadding, viewModel)

            }
        }
    }
}

@Composable
fun MapButton(viewModel: VisitViewModel) {
    FloatingActionButton(
        onClick = { viewModel.setShowMapDialog(true) },
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.secondary,
        shape = CircleShape,
        modifier = Modifier.padding(bottom = 16.dp)
    ) {
        Icon(Icons.Filled.Place, "Map")
    }
}

@Composable
fun VisitContent(innerPadding: PaddingValues, viewModel: VisitViewModel) {
    val client = viewModel.client!!
    val scrollState = rememberScrollState()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .verticalScroll(scrollState)
    ) {
        ClientCard(client.dCliente)

        Divider()

        SelectDirection(viewModel = viewModel, client = client)

        SelectResult(viewModel)

        CameraSection(viewModel)

        Comment(viewModel)

        FullScreenMapDialog(viewModel)

        Spacer(modifier = Modifier.weight(1f))

        RegisterButton(viewModel)
    }
}

@Composable
fun SelectDirection(viewModel: VisitViewModel, client: Client) {
    MyDropDawnMenu(
        selectionOptions = client.direcciones.map { it.direccion },
        init = viewModel.initScreen,
        label = "Almacén",
        placeholder = "Seleccione una dirección",
        onSelectionChange = { index ->
            viewModel.updateDirectionSelectedIndex(index)
        }
    )
}

@Composable
fun SelectResult(viewModel: VisitViewModel) {
    MyDropDawnMenu(
        selectionOptions = viewModel.results.map { it.description },
        init = viewModel.initScreen,
        label = "Resultado",
        placeholder = "Seleccione un resultado",
        onSelectionChange = { index ->
            viewModel.updateResultSelectedIndex(index)
        }
    )
}

@Composable
fun CameraSection(viewModel: VisitViewModel) {
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
fun Comment(viewModel: VisitViewModel) {
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
            .padding(4.dp)
    )
}

@Composable
fun FullScreenMapDialog(viewModel: VisitViewModel) {
    if (viewModel.showMapDialog) {
        if (viewModel.selectedDirectionIndex == null) {
            MyAlertDialog(
                onDismissRequest = { viewModel.setShowMapDialog(false) },
                onConfirmation = { viewModel.setShowMapDialog(false) },
                dialogTitle = "Sin dirección seleccionada",
                dialogText = "Porfavor seleccione una direccion antes de continuar."
            )
        } else {
            Dialog(
                onDismissRequest = { viewModel.setShowMapDialog(false) },
                properties = DialogProperties(usePlatformDefaultWidth = false)
            ) {
                MyMapComposable(viewModel.getDirectionCoords())
            }
        }
    }
}


@Composable
fun RegisterButton(viewModel: VisitViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!viewModel.initScreen) {
            if (viewModel.selectedDirectionIndex == null) {
                WarningText("Seleccione un almacén.")
            }
            if (viewModel.selectedResultIndex == null) {
                WarningText("Seleccione un resultado.")
            }
            if (viewModel.imageUris.isEmpty()) {
                WarningText("Adjunte al menos una fotos.")
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
