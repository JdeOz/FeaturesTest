package com.test.featurestest.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDropDawnMenu(
    modifier: Modifier = Modifier,
    selectionOptions: List<String>,
    init: Boolean,
    label: String,
    placeholder: String,
    preSelected: Boolean = false,
    onSelectionChange: (Int) -> Unit

) {
    var initSelected = ""
    if (preSelected) {
        initSelected = selectionOptions[0]
    }

    var isExpanded by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf(initSelected) }

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { isExpanded = it },
        modifier = modifier.padding(4.dp)
    ) {
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            readOnly = true,
            isError = selected.isEmpty() && !init,
            label = { Text(text = label) },
            placeholder = { Text(text = placeholder) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
            },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )
        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .exposedDropdownSize()
        ) {
            selectionOptions.forEachIndexed() { index, selectionOption ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = selectionOption,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    },
                    onClick = {
                        onSelectionChange(index)
                        selected = selectionOptions[index]
                        isExpanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}