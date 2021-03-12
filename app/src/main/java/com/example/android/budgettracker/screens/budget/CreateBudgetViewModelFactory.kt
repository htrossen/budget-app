package com.example.android.budgettracker.screens.budget

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.budgettracker.database.BudgetDatabaseDao

class CreateBudgetViewModelFactory(
  private val budgetKey: Long,
  private val dataSource: BudgetDatabaseDao,
  private val application: Application) : ViewModelProvider.Factory {

  @Suppress("unchecked_cast")
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    if (modelClass.isAssignableFrom(CreateBudgetViewModel::class.java)) {
      return CreateBudgetViewModel(budgetKey, dataSource, application) as T
    }
    throw IllegalArgumentException("Unknown ViewModel class")
  }
}