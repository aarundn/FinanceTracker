package com.example.financetracker.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val type: String,
    val category: String,
    val amount: Double,
    val date: Long,
    val notes: String?
)