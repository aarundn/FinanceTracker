package com.example.financetracker.presentation.features.details

import com.example.domain.model.TransactionType


object TransDetailsContract {
    
    // State - represents the UI state
    data class State(
        val transactionId: Int? = null,
        val transactionType: TransactionType = TransactionType.EXPENSE,
        val category: String = "",
        val amount: String = "",
        val date: Long = System.currentTimeMillis(),
        val notes: String = "",
        val isValid: Boolean = false,
        val isLoading: Boolean = true,
        val isSaved: Boolean = false,
        val showDatePicker: Boolean = false,
        val categoryError: String? = null,
        val amountError: String? = null,
        val error: String? = null
    )
    
    // Events - user actions that trigger state changes
    sealed class Event {
        data class LoadTransaction(val transactionId: Int) : Event()
        data class UpdateTransactionType(val type: TransactionType) : Event()
        data class UpdateCategory(val category: String) : Event()
        data class UpdateAmount(val amount: String) : Event()
        data class UpdateDate(val date: Long) : Event()
        data class UpdateNotes(val notes: String) : Event()
        object ShowDatePicker : Event()
        object HideDatePicker : Event()
        object SaveTransaction : Event()
        object ClearError : Event()
        object NavigateBack : Event()
    }
    
    // Side Effects - one-time events that should be handled by the UI
    sealed class SideEffect {
        data class ShowSnackbar(val message: String) : SideEffect()
        object NavigateBack : SideEffect()
        object TransactionSaved : SideEffect()
    }
}