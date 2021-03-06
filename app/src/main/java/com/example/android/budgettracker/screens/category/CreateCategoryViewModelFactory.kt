package com.example.android.budgettracker.screens.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.budgettracker.database.BudgetDatabaseDao

class CreateCategoryViewModelFactory(
  private val budgetKey: Long,
  private val categoryKey: Long,
  private val dataSource: BudgetDatabaseDao) : ViewModelProvider.Factory {
  @Suppress("unchecked_cast")
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    if (modelClass.isAssignableFrom(CreateCategoryViewModel::class.java)) {
      return CreateCategoryViewModel(budgetKey, categoryKey, dataSource) as T
    }
    throw IllegalArgumentException("Unknown ViewModel class")
  }
}