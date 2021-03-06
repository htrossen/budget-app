package com.example.android.budgettracker.screens.item

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.budgettracker.database.BudgetDatabaseDao
import com.example.android.budgettracker.database.BudgetItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ItemDetailListViewModel(
  private val budgetKey: Long = 0L,
  private val category: String = "",
  private val month: String = "",
  private val year: Int = 0,
  val database: BudgetDatabaseDao) : ViewModel() {

  private val viewModelJob = Job()

  private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

  var items = MutableLiveData<List<BudgetItem>>()

  private val _navigateToCreateItem = MutableLiveData<Pair<Long,Long>>()
  val navigateToCreateItem : LiveData<Pair<Long,Long>>
    get() = _navigateToCreateItem

  fun doneNavigating() {
    _navigateToCreateItem.value = null
  }

  private val _name = MutableLiveData<String>()
  val name : LiveData<String>
    get() = _name

  private val _date = MutableLiveData<String>()
  val date : LiveData<String>
    get() = _date

  private suspend fun getItems(): List<BudgetItem> {
    return withContext(Dispatchers.IO) {
      val items = database.getItemDetails(budgetKey, category, month, year)
      items
    }
  }

  private suspend fun deleteItem(itemId: Long) {
    withContext(Dispatchers.IO) {
      database.deleteItem(itemId)
    }
  }

  fun onItemClickedEdit(itemId: Long) {
    _navigateToCreateItem.value = Pair(budgetKey, itemId)
    println("testing: item clicked edit")
  }

  fun onItemClickedDelete(itemId: Long) {
    uiScope.launch {
      deleteItem(itemId)
    }
    println("testing: item clicked delete")
  }

  init {
    initializeItemDetailList()
  }

  private fun initializeItemDetailList() {
    uiScope.launch {
      items.value = getItems()
    }
      _name.value = String.format("%s Expenses", category)
      _date.value = String.format("in %s %d", month, year)
  }

  override fun onCleared() {
    super.onCleared()
    viewModelJob.cancel()
  }
}
