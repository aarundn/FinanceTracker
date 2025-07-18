package com.example.financetracker.di

import com.example.financetracker.data.repositories.TransactionRepositoryImpl
import com.example.financetracker.domain.repositories.TransactionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Singleton
    @Binds
    abstract fun bindTransactionsRepository(
        transactionImpl : TransactionRepositoryImpl
    ): TransactionRepository

}