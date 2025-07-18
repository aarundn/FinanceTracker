package com.example.financetracker.domain.usecases

import com.example.financetracker.domain.model.Transaction
import com.example.financetracker.domain.repositories.TransactionRepository
import javax.inject.Inject

class AddTransactionUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend fun invoke(transaction: Transaction)  =
        transactionRepository.addTransaction(transaction = transaction)
}