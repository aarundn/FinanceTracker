package com.example.financetracker.presentation.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Details : Screen("details/{transactionId}") {
        fun createRoute(transactionId: Int) = "details/$transactionId"
    }
} 