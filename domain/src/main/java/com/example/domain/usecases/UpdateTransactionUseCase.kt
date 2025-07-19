package com.example.domain.usecases

import com.example.domain.model.Transaction
import com.example.domain.repositories.TransactionRepository
import javax.inject.Inject

class UpdateTransactionUseCase @Inject constructor(
    private val  transactionRepository: TransactionRepository
) {
    suspend fun invoke(transaction: Transaction) =
        transactionRepository.updateTransaction(transaction = transaction)
}