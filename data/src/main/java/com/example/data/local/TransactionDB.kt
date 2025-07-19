package com.example.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.model.TransactionEntity

@Database(
    entities = [TransactionEntity::class],
    version = 1,
    exportSchema = false
)
abstract class TransactionDB : RoomDatabase() {
    abstract val transactionDao: TransactionDao
}