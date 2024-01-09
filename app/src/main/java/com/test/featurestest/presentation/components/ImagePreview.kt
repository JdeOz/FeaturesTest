package com.test.featurestest.presentation.components

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun ImageCards(
    images: List<Uri>,
    popImage: (Uri) -> Unit,
    show: () -> Unit

) {
    val cardModifier = Modifier
        .height(256.dp)
        .width(160.dp)
        .padding(end = 4.dp)

    Text(
        text = "Imagenes adjuntas",
        modifier = Modifier.padding(8.dp)
    )
    Row(
        modifier = Modifier
            .padding(8.dp)
            .horizontalScroll(rememberScrollState())
    ) {
        images.forEach { image ->
            ImageCard(cardModifier, image = image) { popImage(image) }
        }
        if (images.size < 2) {
            AddImageCard(modifier = cardModifier) { show() }
        }
    }
}

@Composable
fun ImageCard(modifier: Modifier, image: Uri, popImage: () -> Unit) {
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
                onClick = { popImage() },
                modifier = Modifier
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Agregar imagen",
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier
                        .size(32.dp)
                        .background(color = MaterialTheme.colorScheme.error, shape = CircleShape)
                )
            }
        }
    }
}

@Composable
fun AddImageCard(modifier: Modifier, showCameraDialog: () -> Unit) {
    Card(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.secondary)
                .clickable { showCameraDialog() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.AddCircle,
                contentDescription = "Agregar imagen",
                tint = MaterialTheme.colorScheme.onTertiary,
                modifier = Modifier.size(72.dp)
            )
        }
    }
}