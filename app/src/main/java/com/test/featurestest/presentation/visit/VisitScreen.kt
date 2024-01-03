package com.test.featurestest.presentation.visit

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.test.featurestest.domain.model.Client
import com.test.featurestest.presentation.components.CameraUi
import com.test.featurestest.presentation.components.ClientCard
import com.test.featurestest.presentation.components.MyDropDawnMenu
import com.test.featurestest.presentation.components.MyScaffold
import com.test.featurestest.presentation.components.WarningText
import com.test.featurestest.presentation.components.WithPermissions
import timber.log.Timber

@Composable
fun VisitScreen(
    navController: NavHostController,
    clientId: String,
    viewModel: VisitViewModel = hiltViewModel()
) {
    val client = viewModel.client
    val results = viewModel.results
    val isLoading = viewModel.isLoading
    val error = viewModel.error

    val context = LocalContext.current


    LaunchedEffect(Unit) {
        viewModel.loadData(clientId)
    }

    MyScaffold(
        navController = navController,
        title = "Registrar Visita",
        floatingActionButton = {
            FloatingActionButton(
                onClick = {  },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.secondary,
                shape = CircleShape,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Icon(Icons.Filled.Place, "Map")
            }
        }
    ) { innerPadding ->
        when {
            isLoading -> {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(innerPadding),
                )
            }

            error != null -> {
                Timber.d(error)
            }

            client != null && results.isNotEmpty() -> {
                WithPermissions(
                    permissions = listOf(
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.CAMERA,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ),
                    onPermissionsGranted = {}
                ) {
                    VisitContent(innerPadding, context, viewModel)
                }
            }
        }
    }
}

@Composable
fun VisitContent(innerPadding: PaddingValues, context: Context, viewModel: VisitViewModel) {
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

        ImageCards(viewModel)

        Comment(viewModel)

        Spacer(modifier = Modifier.weight(1f))

        RegisterButton(viewModel)
    }
}

@Composable
fun ImageCards(viewModel: VisitViewModel) {
    val scrollState = rememberScrollState()
    FullScreenCameraDialog(viewModel, onDismissRequest = {
        viewModel.setShowCameraDialog(false)
    })

    val cardModifier = Modifier
        .height(256.dp)
        .width(160.dp)
        .padding(end = 4.dp)

    Text(
        text = "Imagenes adjuntas",
        modifier = Modifier.padding(8.dp),
        color = Color.Gray,
        fontWeight = FontWeight.Bold
    )
    Row(
        modifier = Modifier
            .padding(8.dp)
            .horizontalScroll(scrollState)
    ) {
        viewModel.imageUri.forEach { image ->
            ImageCard(viewModel = viewModel, cardModifier, image = image)
        }

        Card(
            modifier = cardModifier
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.LightGray)
                    .clickable { viewModel.setShowCameraDialog(true) },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AddCircle,
                    contentDescription = "Agregar imagen",
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(72.dp)
                )
            }
        }
    }


}

@Composable
fun ImageCard(viewModel: VisitViewModel, modifier: Modifier, image: Uri) {
    Card(
        modifier = modifier,
    ) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopEnd
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(image)
                    .build(),
                contentDescription = "image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth()
            )

            IconButton(
                onClick = {viewModel.popUriImage(image) },
                modifier = Modifier
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Agregar imagen",
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(32.dp).background(color = MaterialTheme.colorScheme.error,shape = CircleShape)
                )
            }
        }
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
fun FullScreenCameraDialog(viewModel: VisitViewModel, onDismissRequest: () -> Unit) {
    if (viewModel.showCameraDialog) {
        Dialog(
            onDismissRequest = onDismissRequest,
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            CameraUi(
                modifier = Modifier.fillMaxSize(),
                onPhotoTaken = { uri ->
                    viewModel.addUriImage(uri)
                    viewModel.setShowCameraDialog(false)
                }
            )
        }
    }
}

@Composable
fun imageDialog(viewModel: VisitViewModel, onDismissRequest: () -> Unit) {
    if (viewModel.showImageDialog) {
        Dialog(onDismissRequest = onDismissRequest) {

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
            if (!viewModel.isSelectedDirection) {
                WarningText("Seleccione un almacén.")
            }
            if (!viewModel.isSelectedResult) {
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
