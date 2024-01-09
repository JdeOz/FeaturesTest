package com.test.featurestest.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun DateField(
    dateText: String,
    init: Boolean,
    onDatePicked: (String) -> Unit
) {
    var showDateDialog by remember { mutableStateOf(false) }
    DateDialog(
        show = showDateDialog,
        notShow = { showDateDialog = false },
        onDatePicked = onDatePicked
    )
    OutlinedTextField(
        value = dateText,
        onValueChange = {},
        label = { Text("Fecha de operación") },
        readOnly = true,
        isError = dateText.isEmpty() && !init,
        modifier = Modifier
            .fillMaxWidth()
            .imePadding()
            .padding(4.dp),
        trailingIcon = {
            IconButton(onClick = { showDateDialog = true }) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Icono de fecha"
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateDialog(show: Boolean, notShow: () -> Unit, onDatePicked: (String) -> Unit) {
    val currentDate =
        LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

    val state = rememberDatePickerState(initialSelectedDateMillis = currentDate)

    LaunchedEffect(key1 = state) {
        val formattedDate = parseDate(state)
        if (formattedDate.isNotEmpty()) {
            onDatePicked(formattedDate)
        }
    }

    if (show) {
        DatePickerDialog(
            onDismissRequest = { notShow() },
            confirmButton = {
                Button(
                    onClick = {
                        onDatePicked(parseDate(state))
                        notShow()
                    }) {
                    Text(text = "Confirmar")
                }
            }
        ) {
            DatePicker(state = state)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
fun parseDate(state: DatePickerState): String {
    val date = state.selectedDateMillis
    if (date != null) {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val localDate = Instant.ofEpochMilli(date).atZone(ZoneId.systemDefault()).toLocalDate()
        return formatter.format(localDate)
    }
    return ""
}