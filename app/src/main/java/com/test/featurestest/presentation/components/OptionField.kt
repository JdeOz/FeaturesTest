package com.test.featurestest.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun OptionField(
    optionIndex: Int?,
    init: Boolean,
    title: String,
    selectionOptions: List<String>,
    onSelectedOption: (Int) -> Unit
) {
    var showOptionDialog by remember { mutableStateOf(false) }
    MenuDialog(
        show = showOptionDialog,
        optionIndex = optionIndex,
        title = title,
        selectionOptions = selectionOptions,
        notShow = { showOptionDialog = false },
        onSelectedOption = onSelectedOption
    )
    OutlinedTextField(
        value = if (optionIndex != null) selectionOptions[optionIndex] else "",
        onValueChange = {},
        label = { Text(title) },
        readOnly = true,
        isError = optionIndex == null && !init,
        modifier = Modifier
            .fillMaxWidth()
            .imePadding()
            .padding(4.dp),
        trailingIcon = {
            IconButton(onClick = { showOptionDialog = true }) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Icono de menu"
                )
            }
        }
    )
}

@Composable
fun MenuDialog(
    show: Boolean,
    optionIndex: Int?,
    title: String,
    selectionOptions: List<String>,
    notShow: () -> Unit,
    onSelectedOption: (Int) -> Unit
) {

    if (show) {
        Dialog(
            onDismissRequest = { notShow() },
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(512.dp)
//                    .padding(vertical = 96.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(8.dp)
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = title, fontWeight = FontWeight.Bold, modifier = Modifier.padding(16.dp))
                Column ( modifier = Modifier.verticalScroll(rememberScrollState())){
                    selectionOptions.forEachIndexed { index, textOption ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .clickable(onClick = {
                                    onSelectedOption(index)
                                    notShow()
                                }),
                            shape = RoundedCornerShape(8.dp),
                            border = if (optionIndex != null && optionIndex == index) BorderStroke(
                                2.dp,
                                MaterialTheme.colorScheme.primary
                            ) else BorderStroke(
                                1.dp,
                                MaterialTheme.colorScheme.outline
                            ),
                            colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                        ) {
                            Text(
                                text = textOption, modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}