package com.example.financetracker.domain.usecases

import com.example.financetracker.domain.repositories.TransactionRepository
import javax.inject.Inject

class DeleteTransactionUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend fun invoke (id: Int) =
        transactionRepository.deleteTransaction(id = id)
}