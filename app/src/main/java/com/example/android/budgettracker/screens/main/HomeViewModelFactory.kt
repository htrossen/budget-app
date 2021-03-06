package com.example.android.budgettracker.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.budgettracker.database.BudgetDatabaseDao

class HomeViewModelFactory(
  private val budgetKey: Long,
  private val dataSource: BudgetDatabaseDao) : ViewModelProvider.Factory {
  @Suppress("unchecked_cast")
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
      return HomeViewModel(budgetKey, dataSource) as T
    }
    throw IllegalArgumentException("Unknown ViewModel class")
  }
}