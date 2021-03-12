package com.example.android.budgettracker.screens.title

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.budgettracker.database.BudgetDatabaseDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TitleViewModel(val database: BudgetDatabaseDao) : ViewModel() {

  private var viewModelJob = Job()

  private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

  val names = database.getBudgetNames()

  private val _count = MutableLiveData<Int>()
  val count : LiveData<Int>
    get() = _count

  init {
    initializeBudget()
  }

  private suspend fun count(): Int {
    return withContext(Dispatchers.IO) {
      val count = database.getBudgetCount()
      count
    }
  }

  private fun initializeBudget() {
    uiScope.launch {
      _count.value = count()
    }
  }

  override fun onCleared() {
    super.onCleared()
    viewModelJob.cancel()
  }
}