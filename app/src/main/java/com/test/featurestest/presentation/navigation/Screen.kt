package com.test.featurestest.presentation.navigation

sealed class Screen(val route: String) {
    data object FeatureSelector : Screen("FeatureSelector")
    data object CallLog : Screen("CallLog/{clientId}") {
        fun createRoute(clientId: String) = "CallLog/$clientId"
    }

    data object Visit : Screen("Visit/{clientId}") {
        fun createRoute(clientId: String) = "Visit/$clientId"
    }
}