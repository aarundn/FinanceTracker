package com.example.financetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.financetracker.presentation.features.details.TransDetailsScreen
import com.example.financetracker.presentation.features.home.TransHomeScreen
import com.example.financetracker.presentation.navigation.Screen
import com.example.financetracker.ui.theme.FinanceTrackerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FinanceTrackerTheme {
                FinanceTrackerApp()
            }
        }
    }
}

@Composable
fun FinanceTrackerApp() {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            TransHomeScreen(
                onNavigateToDetails = { transactionId ->
                    if (transactionId == -1) {
                        navController.navigate(Screen.Details.createRoute(-1))
                    } else {
                        navController.navigate(Screen.Details.createRoute(transactionId))
                    }
                }
            )
        }
        
        composable(
            route = Screen.Details.route,
            arguments = listOf(
                navArgument("transactionId") {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) { backStackEntry ->
            val transactionId = backStackEntry.arguments?.getInt("transactionId")
            TransDetailsScreen(
                transactionId = if (transactionId == -1) null else transactionId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}