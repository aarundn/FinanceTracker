package com.example.financetracker.presentation.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Transaction
import com.example.domain.model.TransactionType
import com.example.domain.usecases.AddTransactionUseCase
import com.example.domain.usecases.DeleteTransactionUseCase
import com.example.domain.usecases.GetAllTransactionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransHomeViewModel @Inject constructor(
    private val getAllTransactionsUseCase: GetAllTransactionsUseCase,
    private val addTransactionUseCase: AddTransactionUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(TransHomeContract.State())
    val state: StateFlow<TransHomeContract.State> = _state.asStateFlow()

    private val _sideEffects = MutableSharedFlow<TransHomeContract.SideEffect>()
    val sideEffects: SharedFlow<TransHomeContract.SideEffect> = _sideEffects.asSharedFlow()

    init {
        onEvent(TransHomeContract.Event.LoadTransactions)
    }

    fun onEvent(event: TransHomeContract.Event) {
        when (event) {
            is TransHomeContract.Event.LoadTransactions -> {
                loadTransactions()
            }
            is TransHomeContract.Event.RefreshTransactions -> {
                refreshTransactions()
            }
            is TransHomeContract.Event.ShowAddDialog -> {
                _state.value = _state.value.copy(showAddDialog = true)
            }
            is TransHomeContract.Event.HideAddDialog -> {
                _state.value = _state.value.copy(showAddDialog = false)
            }
            is TransHomeContract.Event.ClearError -> {
                _state.value = _state.value.copy(error = null)
            }
            is TransHomeContract.Event.AddTransaction -> {
                addTransaction(event.transaction)
            }
            is TransHomeContract.Event.DeleteTransaction -> {
                deleteTransaction(event.transactionId)
            }
            is TransHomeContract.Event.NavigateToDetails -> {
                viewModelScope.launch {
                    _sideEffects.emit(TransHomeContract.SideEffect.NavigateToDetails(event.transactionId))
                }
            }
        }
    }

    private fun loadTransactions() {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isLoading = true, error = null)
                
                getAllTransactionsUseCase.invoke().collect { transactions ->
                    val totalIncome = transactions
                        .filter { it.type == TransactionType.INCOME }
                        .sumOf { it.amount }
                    val totalExpenses = transactions
                        .filter { it.type == TransactionType.EXPENSE }
                        .sumOf { it.amount }
                    val balance = totalIncome - totalExpenses

                    _state.value = _state.value.copy(
                        transactions = transactions,
                        totalIncome = totalIncome,
                        totalExpenses = totalExpenses,
                        balance = balance,
                        isLoading = false,
                        isRefreshing = false
                    )
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    error = e.message ?: "Unknown error occurred",
                    isLoading = false,
                    isRefreshing = false
                )
                _sideEffects.emit(TransHomeContract.SideEffect.ShowSnackbar("Failed to load transactions"))
            }
        }
    }

    private fun refreshTransactions() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isRefreshing = true)
            loadTransactions()
        }
    }

    private fun addTransaction(transaction: Transaction) {
        viewModelScope.launch {
            try {
                addTransactionUseCase.invoke(transaction)
                _state.value = _state.value.copy(showAddDialog = false)
                _sideEffects.emit(TransHomeContract.SideEffect.TransactionAdded)
                _sideEffects.emit(TransHomeContract.SideEffect.ShowSnackbar("Transaction added successfully"))
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    error = e.message ?: "Failed to add transaction"
                )
                _sideEffects.emit(TransHomeContract.SideEffect.ShowSnackbar("Failed to add transaction"))
            }
        }
    }

    private fun deleteTransaction(transactionId: Int) {
        viewModelScope.launch {
            try {
                deleteTransactionUseCase.invoke(transactionId)
                _sideEffects.emit(TransHomeContract.SideEffect.TransactionDeleted)
                _sideEffects.emit(TransHomeContract.SideEffect.ShowSnackbar("Transaction deleted successfully"))
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    error = e.message ?: "Failed to delete transaction"
                )
                _sideEffects.emit(TransHomeContract.SideEffect.ShowSnackbar("Failed to delete transaction"))
            }
        }
    }
}