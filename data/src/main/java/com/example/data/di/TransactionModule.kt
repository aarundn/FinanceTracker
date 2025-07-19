package com.example.data.di

import android.content.Context
import androidx.room.Room
import com.example.data.local.TransactionDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TransactionModule {

    @Provides
    @Singleton
    fun provideTransactionDatabase(@ApplicationContext context: Context): TransactionDB {
        return Room.databaseBuilder(
            context,
            TransactionDB::class.java,
            "quran_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideSurahDao(transactionDatabase: TransactionDB) =
        transactionDatabase.transactionDao
}