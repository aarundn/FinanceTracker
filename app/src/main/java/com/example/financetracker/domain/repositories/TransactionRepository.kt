package com.example.financetracker.domain.repositories

import com.example.financetracker.domain.model.Transaction
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    suspend fun addTransaction(transaction: Transaction)
    suspend fun deleteTransaction(id: Int)
    fun getAllTransactions(): Flow<List<Transaction>>
    suspend fun updateTransaction(transaction: Transaction)
}