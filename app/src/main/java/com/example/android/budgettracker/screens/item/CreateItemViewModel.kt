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
import org.threeten.bp.LocalDate
import org.threeten.bp.Month

class CreateItemViewModel(private val budgetKey: Long = 0L, private val itemKey: Long = 0L, val database: BudgetDatabaseDao) : ViewModel() {

  private val viewModelJob = Job()

  private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

  val categoryNames = database.getCategoryNames(budgetKey)

  private var item = MutableLiveData<BudgetItem?>()

  private val _newText = MutableLiveData<Boolean>()
  val newText : LiveData<Boolean>
    get() = _newText

  private val _description = MutableLiveData<String>()
  val description : LiveData<String>
    get() = _description

  private val _categories = MutableLiveData<List<String>>()
  val categories : LiveData<List<String>>
    get() = _categories

  private val _category = MutableLiveData<String>()
  val category : LiveData<String>
    get() = _category

  private val _year = MutableLiveData<Int>()
  val year : LiveData<Int>
    get() = _year

  private val _month = MutableLiveData<Int>()
  val month : LiveData<Int>
    get() = _month

  private val _day = MutableLiveData<Int>()
  val day : LiveData<Int>
    get() = _day

  private val _cost = MutableLiveData<String>()
  val cost : LiveData<String>
    get() = _cost

  private val _group = MutableLiveData<String>()
  val group : LiveData<String>
    get() = _group

  private val _navigateToHome= MutableLiveData<Long>()
  val navigateToHome : LiveData<Long>
    get() = _navigateToHome

  fun doneNavigating() {
    _navigateToHome.value = null
  }

  init {
    initializeItem()
  }

  private suspend fun getItemById(id: Long): BudgetItem? {
    return withContext(Dispatchers.IO) {
      var item = database.getItemWithId(id)
      item
    }
  }

  private suspend fun insert(item: BudgetItem) {
    withContext(Dispatchers.IO) {
      database.insertItem(item)
    }
  }

  private suspend fun update(item: BudgetItem) {
    withContext(Dispatchers.IO) {
      database.updateItem(item)
    }
  }

  fun categoryListUpdate() {
    _categories.value =  listOf("Choose an Expense Category") + categoryNames.value.orEmpty()
  }

  private fun initializeItem() {
    uiScope.launch {
      if (itemKey > 0) {
        _newText.value = false
        item.value = getItemById(itemKey)
        _description.value = item.value?.description
        _category.value = item.value?.category
        _year.value = item.value?.year
        _month.value = Month.valueOf(item.value?.month!!.toUpperCase()).value - 1
        _day.value = item.value?.day!! - 1
        _cost.value = item.value?.cost.toString()
        _group.value = item.value?.split.toString()
      }
      else {
        _newText.value = true
        val localDate = LocalDate.now()
        _year.value = localDate.year
        _month.value = localDate.monthValue - 1
        _day.value = localDate.dayOfMonth - 1
        _group.value = "1"
      }
    }
  }

  fun insertOrAddItem(description: String, category: String, month: String, day: Int, cost: String, split: String) {
    uiScope.launch {
      val newItem = BudgetItem()
      newItem.budgetId = budgetKey
      newItem.description = description
      newItem.category = category
      newItem.cost = cost.toDouble()
      newItem.split = split.toInt()
      newItem.spent = newItem.cost.div(newItem.split)
      newItem.month = month
      newItem.day = day + 1
      newItem.year = _year.value ?: LocalDate.now().year
      if (item.value == null) {
        insert(newItem)
      } else {
        newItem.id = itemKey
        update(newItem)
      }
    }
    _navigateToHome.value = budgetKey
  }

  override fun onCleared() {
    super.onCleared()
    viewModelJob.cancel()
  }
}
