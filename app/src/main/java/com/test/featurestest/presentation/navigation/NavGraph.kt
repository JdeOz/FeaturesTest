package com.test.featurestest.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.test.featurestest.presentation.call_log.CallLogScreen
import com.test.featurestest.presentation.deposit.DepositScreen
import com.test.featurestest.presentation.receipt.ReceiptScreen
import com.test.featurestest.presentation.feature_selector.FeatureSelectorScreen
import com.test.featurestest.presentation.visit.VisitScreen

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
        composable(Screen.Visit.route) { backStackEntry ->
            val clientId = backStackEntry.arguments?.getString("clientId") ?: return@composable
            VisitScreen(navController, clientId)
        }
        composable(Screen.Receipt.route) { backStackEntry ->
            val clientId = backStackEntry.arguments?.getString("clientId") ?: return@composable
            ReceiptScreen(navController, clientId)
        }
        composable(Screen.Deposit.route) { backStackEntry ->
            val clientId = backStackEntry.arguments?.getString("clientId") ?: return@composable
            DepositScreen(navController, clientId)
        }
    }

}