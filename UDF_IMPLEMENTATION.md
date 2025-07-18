# UDF (Unidirectional Data Flow) Implementation

This document describes the UDF pattern implementation in the Finance Tracker Android app.

## Overview

The app now uses the Unidirectional Data Flow pattern with the following components:

- **State**: Represents the UI state
- **Events**: User actions that trigger state changes
- **Side Effects**: One-time events that should be handled by the UI

## Architecture Components

### 1. Contract Classes

Each screen has a contract class that defines the State, Events, and Side Effects:

#### TransHomeContract
```kotlin
object TransHomeContract {
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
    
    sealed class SideEffect {
        data class ShowSnackbar(val message: String) : SideEffect()
        data class NavigateToDetails(val transactionId: Int) : SideEffect()
        object TransactionAdded : SideEffect()
        object TransactionDeleted : SideEffect()
    }
}
```

#### TransDetailsContract
```kotlin
object TransDetailsContract {
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
    
    sealed class SideEffect {
        data class ShowSnackbar(val message: String) : SideEffect()
        object NavigateBack : SideEffect()
        object TransactionSaved : SideEffect()
    }
}
```

### 2. ViewModels

ViewModels handle the business logic and manage state:

```kotlin
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

    fun onEvent(event: TransHomeContract.Event) {
        when (event) {
            is TransHomeContract.Event.LoadTransactions -> {
                loadTransactions()
            }
            // ... other event handlers
        }
    }
}
```

### 3. UI Components

Screens use the UDF pattern with proper state management and side effect handling:

```kotlin
@Composable
fun TransHomeScreen(
    onNavigateToDetails: (Int) -> Unit,
    viewModel: TransHomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = rememberSnackbarHostState()
    
    // Handle side effects
    SideEffectHandler(
        sideEffects = viewModel.sideEffects,
        onSideEffect = { effect ->
            when (effect) {
                is TransHomeContract.SideEffect.NavigateToDetails -> {
                    onNavigateToDetails(effect.transactionId)
                }
                is TransHomeContract.SideEffect.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
            }
        }
    )
    
    // UI implementation
}
```

### 4. Side Effect Handler

A utility composable for handling side effects efficiently:

```kotlin
@Composable
fun <T> SideEffectHandler(
    sideEffects: SharedFlow<T>,
    onSideEffect: (T) -> Unit
) {
    var lastSideEffect by remember { mutableStateOf<T?>(null) }
    
    LaunchedEffect(sideEffects) {
        sideEffects.collect { sideEffect ->
            if (sideEffect != lastSideEffect) {
                lastSideEffect = sideEffect
                onSideEffect(sideEffect)
            }
        }
    }
}
```

## Benefits of UDF Implementation

1. **Predictable State Management**: All state changes flow through a single path
2. **Testability**: Easy to test state changes and side effects
3. **Debugging**: Clear separation of concerns makes debugging easier
4. **Maintainability**: Code is more organized and easier to maintain
5. **Reactive UI**: UI automatically updates based on state changes
6. **Side Effect Management**: One-time events are handled properly

## Flow Diagram

```
User Action → Event → ViewModel → State Change → UI Update
                ↓
            Side Effect → UI Handler → One-time Action
```

## Key Features Added

1. **Pull-to-Refresh**: Users can refresh the transaction list
2. **Loading States**: Proper loading indicators
3. **Error Handling**: Comprehensive error handling with user feedback
4. **Snackbar Notifications**: User feedback for actions
5. **Form Validation**: Real-time form validation in the details screen
6. **Navigation**: Proper navigation handling through side effects

## Usage Examples

### Adding a Transaction
1. User taps FAB → `ShowAddDialog` event
2. User fills form → `AddTransaction` event
3. ViewModel processes → State updates + `TransactionAdded` side effect
4. UI shows success message and closes dialog

### Deleting a Transaction
1. User taps delete → `DeleteTransaction` event
2. ViewModel processes → State updates + `TransactionDeleted` side effect
3. UI shows confirmation message

### Navigation
1. User taps edit → `NavigateToDetails` event
2. ViewModel emits → `NavigateToDetails` side effect
3. UI navigates to details screen

This implementation provides a robust, maintainable, and testable architecture for the Finance Tracker app. 