package com.example.financetracker.data.repositories

import com.example.financetracker.data.local.TransactionDao
import com.example.financetracker.domain.mappers.toEntity
import com.example.financetracker.domain.mappers.toTransactionList
import com.example.financetracker.domain.model.Transaction
import com.example.financetracker.domain.repositories.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val transactionDao: TransactionDao
): TransactionRepository {
    override suspend fun addTransaction(transaction: Transaction) {
        transactionDao.insertTransaction(transaction = transaction.toEntity())
    }

    override suspend fun deleteTransaction(id: Int) {
        transactionDao.deleteTransaction(id = id)
    }

    override fun getAllTransactions(): Flow<List<Transaction>> =
        transactionDao.getAllTransactions().map { it.toTransactionList() }

    override suspend fun updateTransaction(transaction: Transaction) {
        transactionDao.updateTransaction(transaction = transaction.toEntity())
    }
}