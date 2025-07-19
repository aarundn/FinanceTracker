package com.example.data.repositories

import com.example.data.local.TransactionDao
import com.example.data.mappers.toEntity
import com.example.data.mappers.toTransactionList
import com.example.domain.model.Transaction
import com.example.domain.repositories.TransactionRepository
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