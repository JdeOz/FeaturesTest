package com.test.featurestest.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RadioButtonGroup(
    modifier: Modifier,
    items: List<String>,
    selection: String,
    onItemClick: ((String) -> Unit)
) {
    Column(modifier = modifier) {
        items.forEach { item ->
            LabelledRadioButton(
                modifier = Modifier.fillMaxWidth(),
                label = item,
                selected = item == selection,
                onClick = {
                    onItemClick(item)
                }
            )
        }
    }
}

@Composable
fun LabelledRadioButton(
    modifier: Modifier = Modifier,
    label: String,
    selected: Boolean,
    onClick: (() -> Unit)?,
    enabled: Boolean = true,
    colors: RadioButtonColors = RadioButtonDefaults.colors()
) {

    Row(
        modifier = modifier
            .height(32.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick,
            enabled = enabled,
            colors = colors
        )

        Text(
            text = label
        )
    }
}