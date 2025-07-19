package com.example.domain.usecases

import com.example.domain.model.Transaction
import com.example.domain.repositories.TransactionRepository
import javax.inject.Inject

class AddTransactionUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend fun invoke(transaction: Transaction)  =
        transactionRepository.addTransaction(transaction = transaction)
}