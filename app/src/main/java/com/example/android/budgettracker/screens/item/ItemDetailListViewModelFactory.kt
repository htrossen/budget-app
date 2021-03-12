package com.example.android.budgettracker.screens.item

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.budgettracker.database.BudgetDatabaseDao

class ItemDetailListViewModelFactory(
  private val budgetKey: Long,
  private val category: String,
  private val month: String,
  private val year: Int,
  private val dataSource: BudgetDatabaseDao) : ViewModelProvider.Factory {
  @Suppress("unchecked_cast")
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    if (modelClass.isAssignableFrom(ItemDetailListViewModel::class.java)) {
      return ItemDetailListViewModel(budgetKey, category, month, year, dataSource) as T
    }
    throw IllegalArgumentException("Unknown ViewModel class")
  }
}