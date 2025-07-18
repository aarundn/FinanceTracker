package com.example.financetracker.domain.usecases

import com.example.financetracker.domain.model.Transaction
import com.example.financetracker.domain.repositories.TransactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllTransactionsUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    fun invoke(): Flow<List<Transaction>> =
        transactionRepository.getAllTransactions()
}