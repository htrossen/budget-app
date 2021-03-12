package com.example.android.budgettracker.screens.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.budgettracker.database.Budget
import com.example.android.budgettracker.database.BudgetDatabaseDao
import com.example.android.budgettracker.database.BudgetHomeCategory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDate

class HomeViewModel(private val budgetKey: Long = 0L, val database: BudgetDatabaseDao) : ViewModel() {

  private val viewModelJob = Job()

  private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

  private val localDate = LocalDate.now()

  private val _year = MutableLiveData(localDate.year)
  val year : LiveData<Int>
    get() = _year

  private val _month = MutableLiveData(localDate.month.name.toLowerCase().capitalize())
  val month : LiveData<String>
    get() = _month

  var needsCategories = MutableLiveData<List<BudgetHomeCategory>>()
  var savingsCategories = MutableLiveData<List<BudgetHomeCategory>>()
  var wantsCategories = MutableLiveData<List<BudgetHomeCategory>>()

  private var budget = MutableLiveData<Budget?>()

  private val _changeDate = MutableLiveData<Boolean>()
  val changeDate : LiveData<Boolean>
    get() = _changeDate

  private val _navigateToCreateItem = MutableLiveData<Long>()
  val navigateToCreateItem : LiveData<Long>
    get() = _navigateToCreateItem

  fun doneNavigating() {
    _navigateToCreateItem.value = null
    _edit.value = false
    _delete.value = false
  }

  private val _edit = MutableLiveData<Boolean>()
  val edit : LiveData<Boolean>
    get() = _edit

  private val _delete = MutableLiveData<Boolean>()
  val delete : LiveData<Boolean>
    get() = _delete

  private val _name = MutableLiveData<String>()
  val name : LiveData<String>
    get() = _name

  private val _date = MutableLiveData<String>()
  val date : LiveData<String>
    get() = _date


  private suspend fun getBudgetById(id: Long): Budget? {
    return withContext(Dispatchers.IO) {
      val budget = database.getBudgetWithId(id)
      budget
    }
  }

  private suspend fun delete() {
    withContext(Dispatchers.IO) {
      database.deleteBudget(budgetKey)
      database.clearCategories(budgetKey)
      database.clearItems(budgetKey)
    }
  }

  private suspend fun getHomeInfo(month: String, year: Int, category: String): List<BudgetHomeCategory> {
    return withContext(Dispatchers.IO) {
      val homeInfo = database.getHomeCategories(budgetKey, month, year, category)
      homeInfo
    }
  }

  init {
    initializeHome()
  }

  private fun initializeHome() {
    _date.value = _month.value + " " + _year.value

    uiScope.launch {
      budget.value = getBudgetById(budgetKey) ?: Budget()
      _name.value = budget.value?.name
      needsCategories.value = getHomeInfo(month.value!!, year.value!!, "Needs")
      savingsCategories.value = getHomeInfo(month.value!!, year.value!!, "Savings")
      wantsCategories.value = getHomeInfo(month.value!!, year.value!!, "Wants")
    }
  }

  fun categoriesUpdate() {
    uiScope.launch {
      needsCategories.value = getHomeInfo(month.value!!, year.value!!, "Needs")
      savingsCategories.value = getHomeInfo(month.value!!, year.value!!, "Savings")
      wantsCategories.value = getHomeInfo(month.value!!, year.value!!, "Wants")
    }
  }

  fun onUpdateDate(month: String, year: Int) {
    _month.value = month
    _year.value = year
    _date.value = _month.value + " " + _year.value
    _changeDate.value = false
  }

  fun onChangeDate() {
    println("testing: date change clicked")
    _changeDate.value = true
  }

  fun onAddItem() {
    _navigateToCreateItem.value = budgetKey
  }

  fun deleteBudget() {
    uiScope.launch {
      delete()
    }
    _delete.value = true
  }

  fun editBudget() {
    println("testing: editing budget")
    _edit.value = true
  }

  override fun onCleared() {
    super.onCleared()
    viewModelJob.cancel()
  }
}
