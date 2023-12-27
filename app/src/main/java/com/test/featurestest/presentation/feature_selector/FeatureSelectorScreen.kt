package com.test.featurestest.presentation.feature_selector

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.test.featurestest.presentation.navigation.Screen

@Composable
fun FeatureSelectorScreen(navController: NavHostController) {
    Column(modifier = Modifier.padding(16.dp)) {
        Button(onClick = { navController.navigate(Screen.CallLog.createRoute("aeaseasdzsx")) }) {
            Text("Registro de Llamadas")
        }
    }
}