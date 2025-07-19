package com.example.financetracker.presentation.features.home

import com.example.domain.model.Transaction

object TransHomeContract {
    
    // State - represents the UI state
    data class State(
        val transactions: List<Transaction> = emptyList(),
        val totalIncome: Double = 0.0,
        val totalExpenses: Double = 0.0,
        val balance: Double = 0.0,
        val isLoading: Boolean = true,
        val error: String? = null,
        val showAddDialog: Boolean = false,
        val isRefreshing: Boolean = false
    )
    
    // Events - user actions that trigger state changes
    sealed class Event {
        object LoadTransactions : Event()
        object RefreshTransactions : Event()
        object ShowAddDialog : Event()
        object HideAddDialog : Event()
        object ClearError : Event()
        data class AddTransaction(val transaction: Transaction) : Event()
        data class DeleteTransaction(val transactionId: Int) : Event()
        data class NavigateToDetails(val transactionId: Int) : Event()
    }
    
    // Side Effects - one-time events that should be handled by the UI
    sealed class SideEffect {
        data class ShowSnackbar(val message: String) : SideEffect()
        data class NavigateToDetails(val transactionId: Int) : SideEffect()
        object TransactionAdded : SideEffect()
        object TransactionDeleted : SideEffect()
    }
}