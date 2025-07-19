package com.example.financetracker.presentation.features.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.domain.model.TransactionType
import kotlinx.coroutines.flow.collectLatest
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransDetailsScreen(
    transactionId: Int?,
    onNavigateBack: () -> Unit,
    viewModel: TransDetailsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState()}

    LaunchedEffect(viewModel.sideEffects){
        viewModel.sideEffects.collectLatest { effect ->
            when (effect) {
                is TransDetailsContract.SideEffect.NavigateBack -> {
                    onNavigateBack()
                }
                is TransDetailsContract.SideEffect.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
                else -> {
                    // Handle other side effects if needed
                }
            }
        }
    }

    
    LaunchedEffect(transactionId) {
        transactionId?.let { 
            viewModel.onEvent(TransDetailsContract.Event.LoadTransaction(it))
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        if (transactionId == null) "Add Transaction" else "Edit Transaction"
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = { 
                        viewModel.onEvent(TransDetailsContract.Event.NavigateBack)
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = { 
                            viewModel.onEvent(TransDetailsContract.Event.SaveTransaction)
                        },
                        enabled = state.isValid
                    ) {
                        Icon(Icons.AutoMirrored.Default.Send, contentDescription = "Save")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Transaction Type Selection
                Card {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Transaction Type",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            TransactionType.values().forEach { type ->
                                FilterChip(
                                    onClick = { 
                                        viewModel.onEvent(TransDetailsContract.Event.UpdateTransactionType(type))
                                    },
                                    label = { Text(type.name) },
                                    selected = state.transactionType == type,
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = if (type == TransactionType.INCOME) 
                                            Color.Green.copy(alpha = 0.2f) 
                                        else 
                                            Color.Red.copy(alpha = 0.2f)
                                    ),
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
                
                // Category Input
                OutlinedTextField(
                    value = state.category,
                    onValueChange = { 
                        viewModel.onEvent(TransDetailsContract.Event.UpdateCategory(it))
                    },
                    label = { Text("Category") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = state.categoryError != null
                )
                if (state.categoryError != null) {
                    Text(
                        text = state.categoryError ?: "Invalid category",
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                
                // Amount Input
                OutlinedTextField(
                    value = state.amount,
                    onValueChange = { 
                        viewModel.onEvent(TransDetailsContract.Event.UpdateAmount(it))
                    },
                    label = { Text("Amount") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = state.amountError != null,
                    prefix = { Text("$") }
                )
                if (state.amountError != null) {
                    Text(
                        text = state.amountError ?: "Invalid amount",
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                
                // Date Selection
                Card {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Date",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                                .format(Date(state.date)),
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { 
                                viewModel.onEvent(TransDetailsContract.Event.ShowDatePicker)
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Select Date")
                        }
                    }
                }
                
                // Notes Input
                OutlinedTextField(
                    value = state.notes,
                    onValueChange = { 
                        viewModel.onEvent(TransDetailsContract.Event.UpdateNotes(it))
                    },
                    label = { Text("Notes (Optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    maxLines = 5
                )
                
                // Error Display
                if (state.error != null) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Red.copy(alpha = 0.1f)
                        )
                    ) {
                        Text(
                            text = state.error ?: "An error occurred",
                            color = Color.Red,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    }
    
    // Date Picker Dialog
    if (state.showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = state.date
        )
        
        DatePickerDialog(
            onDismissRequest = { 
                viewModel.onEvent(TransDetailsContract.Event.HideDatePicker)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { 
                            viewModel.onEvent(TransDetailsContract.Event.UpdateDate(it))
                        }
                        viewModel.onEvent(TransDetailsContract.Event.HideDatePicker)
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { 
                    viewModel.onEvent(TransDetailsContract.Event.HideDatePicker)
                }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

