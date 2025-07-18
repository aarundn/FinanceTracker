package com.example.financetracker.presentation.features.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financetracker.domain.model.Transaction
import com.example.financetracker.domain.model.TransactionType
import com.example.financetracker.domain.usecases.AddTransactionUseCase
import com.example.financetracker.domain.usecases.UpdateTransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransDetailsViewModel @Inject constructor(
    private val addTransactionUseCase: AddTransactionUseCase,
    private val updateTransactionUseCase: UpdateTransactionUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(TransDetailsContract.State())
    val state: StateFlow<TransDetailsContract.State> = _state.asStateFlow()

    private val _sideEffects = MutableSharedFlow<TransDetailsContract.SideEffect>()
    val sideEffects: SharedFlow<TransDetailsContract.SideEffect> = _sideEffects.asSharedFlow()

    fun onEvent(event: TransDetailsContract.Event) {
        when (event) {
            is TransDetailsContract.Event.LoadTransaction -> {
                loadTransaction(event.transactionId)
            }
            is TransDetailsContract.Event.UpdateTransactionType -> {
                updateTransactionType(event.type)
            }
            is TransDetailsContract.Event.UpdateCategory -> {
                updateCategory(event.category)
            }
            is TransDetailsContract.Event.UpdateAmount -> {
                updateAmount(event.amount)
            }
            is TransDetailsContract.Event.UpdateDate -> {
                updateDate(event.date)
            }
            is TransDetailsContract.Event.UpdateNotes -> {
                updateNotes(event.notes)
            }
            is TransDetailsContract.Event.ShowDatePicker -> {
                _state.value = _state.value.copy(showDatePicker = true)
            }
            is TransDetailsContract.Event.HideDatePicker -> {
                _state.value = _state.value.copy(showDatePicker = false)
            }
            is TransDetailsContract.Event.SaveTransaction -> {
                saveTransaction()
            }
            is TransDetailsContract.Event.ClearError -> {
                _state.value = _state.value.copy(error = null)
            }
            is TransDetailsContract.Event.NavigateBack -> {
                viewModelScope.launch {
                    _sideEffects.emit(TransDetailsContract.SideEffect.NavigateBack)
                }
            }
        }
    }

    private fun loadTransaction(transactionId: Int) {
        // In a real app, you would load the transaction from the database
        // For now, we'll just set the ID for editing mode
        _state.value = _state.value.copy(
            transactionId = transactionId,
            isLoading = false
        )
    }

    private fun updateTransactionType(type: TransactionType) {
        _state.value = _state.value.copy(transactionType = type)
        validateForm()
    }

    private fun updateCategory(category: String) {
        _state.value = _state.value.copy(category = category)
        validateForm()
    }

    private fun updateAmount(amount: String) {
        _state.value = _state.value.copy(amount = amount)
        validateForm()
    }

    private fun updateDate(date: Long) {
        _state.value = _state.value.copy(date = date)
        validateForm()
    }

    private fun updateNotes(notes: String) {
        _state.value = _state.value.copy(notes = notes)
        validateForm()
    }

    private fun validateForm() {
        val currentState = _state.value
        var isValid = true
        var categoryError: String? = null
        var amountError: String? = null

        // Validate category
        if (currentState.category.isBlank()) {
            categoryError = "Category is required"
            isValid = false
        }

        // Validate amount
        if (currentState.amount.isBlank()) {
            amountError = "Amount is required"
            isValid = false
        } else {
            try {
                val amount = currentState.amount.toDouble()
                if (amount <= 0) {
                    amountError = "Amount must be greater than 0"
                    isValid = false
                }
            } catch (e: NumberFormatException) {
                amountError = "Invalid amount format"
                isValid = false
            }
        }

        _state.value = currentState.copy(
            isValid = isValid,
            categoryError = categoryError,
            amountError = amountError
        )
    }

    private fun saveTransaction() {
        if (!state.value.isValid) return

        viewModelScope.launch {
            try {
                val amount = state.value.amount.toDouble()
                val transaction = Transaction(
                    id = state.value.transactionId ?: 0,
                    type = state.value.transactionType,
                    category = state.value.category,
                    amount = amount,
                    date = state.value.date,
                    notes = state.value.notes.takeIf { it.isNotBlank() }
                )

                if (state.value.transactionId == null) {
                    // Add new transaction
                    addTransactionUseCase.invoke(transaction)
                } else {
                    // Update existing transaction
                    updateTransactionUseCase.invoke(transaction)
                }

                _state.value = _state.value.copy(
                    error = null,
                    isSaved = true
                )
                
                _sideEffects.emit(TransDetailsContract.SideEffect.TransactionSaved)
                _sideEffects.emit(TransDetailsContract.SideEffect.ShowSnackbar("Transaction saved successfully"))
                _sideEffects.emit(TransDetailsContract.SideEffect.NavigateBack)
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    error = e.message ?: "Failed to save transaction"
                )
                _sideEffects.emit(TransDetailsContract.SideEffect.ShowSnackbar("Failed to save transaction"))
            }
        }
    }
}