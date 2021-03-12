package com.example.android.budgettracker.screens.info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class InfoViewModelFactory(
  private val infoType: String) : ViewModelProvider.Factory {
  @Suppress("unchecked_cast")
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    if (modelClass.isAssignableFrom(InfoViewModel::class.java)) {
      return InfoViewModel(infoType) as T
    }
    throw IllegalArgumentException("Unknown ViewModel class")
  }
}