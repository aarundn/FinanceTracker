package com.example.domain.usecases

import com.example.domain.repositories.TransactionRepository
import javax.inject.Inject

class DeleteTransactionUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend fun invoke (id: Int) =
        transactionRepository.deleteTransaction(id = id)
}