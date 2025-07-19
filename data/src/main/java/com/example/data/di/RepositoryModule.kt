package com.example.data.di

import com.example.data.repositories.TransactionRepositoryImpl
import com.example.domain.repositories.TransactionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindTransactionsRepository(
        transactionImpl : TransactionRepositoryImpl
    ): TransactionRepository

}