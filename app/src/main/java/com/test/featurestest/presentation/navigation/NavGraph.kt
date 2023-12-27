package com.test.featurestest.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.test.featurestest.presentation.call_log.CallLogScreen
import com.test.featurestest.presentation.feature_selector.FeatureSelectorScreen

@Composable
fun NavGraph(startDestination:String = "FeatureSelector"){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.FeatureSelector.route) {
            FeatureSelectorScreen(navController)
        }
        composable(Screen.CallLog.route) { backStackEntry ->
            val clientId = backStackEntry.arguments?.getString("clientId") ?: return@composable
            CallLogScreen(navController, clientId)
        }
    }

}