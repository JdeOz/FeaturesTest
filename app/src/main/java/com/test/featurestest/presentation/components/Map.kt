package com.test.featurestest.presentation.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MyMapComposable(coords: LatLng) {
//    val coords2 = LatLng(-16.39341656171025, -71.53287904737138)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(coords, 15f)
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(
            isMyLocationEnabled = true,
            mapType = MapType.NORMAL
        ),
        uiSettings = MapUiSettings(zoomControlsEnabled = true),
        onMapLoaded = {
        }
    ) {
        Marker(
            state = MarkerState(position = coords),
            title = "Singapur",
            snippet = "Descripción del marcador",
            onClick = {
                true
            }
        )
    }
}