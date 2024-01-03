package com.test.featurestest.presentation.components

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
fun WithPermissions(
    permissions: List<String>,
    onPermissionsGranted: () -> Unit,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val permissionsStatus = remember { mutableStateOf(PermissionStatus.Pending) }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsMap ->
        if (permissionsMap.values.all { it }) {
            permissionsStatus.value = PermissionStatus.Granted
        } else {
            permissionsStatus.value = PermissionStatus.Denied
        }
    }

    LaunchedEffect(key1 = context) {
        launcher.launch(permissions.toTypedArray())
    }

    when (permissionsStatus.value) {
        PermissionStatus.Granted -> onPermissionsGranted()
        PermissionStatus.Denied -> {}
        PermissionStatus.Pending -> {}
    }

    content()
}

enum class PermissionStatus {
    Granted, Denied, Pending
}
