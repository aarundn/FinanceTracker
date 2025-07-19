package com.example.domain.model

data class Transaction (
    val id: Int,
    val type: TransactionType,
    val category: String,
    val amount: Double,
    val date: Long,
    val notes: String?
)

enum class TransactionType { EXPENSE, INCOME }