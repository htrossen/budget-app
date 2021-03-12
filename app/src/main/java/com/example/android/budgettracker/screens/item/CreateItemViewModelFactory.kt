package com.example.android.budgettracker.screens.item

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.budgettracker.database.BudgetDatabaseDao

class CreateItemViewModelFactory(
  private val budgetKey: Long,
  private val itemKey: Long,
  private val dataSource: BudgetDatabaseDao) : ViewModelProvider.Factory {
  @Suppress("unchecked_cast")
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    if (modelClass.isAssignableFrom(CreateItemViewModel::class.java)) {
      return CreateItemViewModel(budgetKey, itemKey, dataSource) as T
    }
    throw IllegalArgumentException("Unknown ViewModel class")
  }
}