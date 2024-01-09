package com.test.featurestest.presentation.feature_selector

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.test.featurestest.presentation.navigation.Screen

@Composable
fun FeatureSelectorScreen(navController: NavHostController) {
    Column(modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars).padding(16.dp)) {
        Button(onClick = { navController.navigate(Screen.CallLog.createRoute("aeaseasdzsx")) }) {
            Text("Registro de Llamadas")
        }
        Button(onClick = { navController.navigate(Screen.Visit.createRoute("aeaseasdzsx")) }) {
            Text("Registro de visitas")
        }
        Button(onClick = { navController.navigate(Screen.Receipt.createRoute("aeaseasdzsx")) }) {
            Text("Recibo de cobranza")
        }
        Button(onClick = { navController.navigate(Screen.Deposit.createRoute("aeaseasdzsx")) }) {
            Text("Depósito de cobranza")
        }
    }
}