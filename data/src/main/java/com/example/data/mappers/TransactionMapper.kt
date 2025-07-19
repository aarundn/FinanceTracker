package com.example.data.mappers

import com.example.data.model.TransactionEntity
import com.example.domain.model.Transaction
import com.example.domain.model.TransactionType

fun Transaction.toEntity(): TransactionEntity {
    return TransactionEntity(
        id = id,
        type = type.name,
        category = category,
        amount = amount,
        date = date,
        notes = notes
    )
}

fun TransactionEntity.toTransaction() : Transaction{
    return Transaction(
        id = id,
        type  = enumValueOf<TransactionType>(type),
        category = category,
        amount = amount,
        date = date,
        notes = notes
    )
}

fun List<Transaction>.toEntityList(): List<TransactionEntity>  = map { it.toEntity() }
fun List<TransactionEntity>.toTransactionList(): List<Transaction> = map { it.toTransaction() }