package com.example.android.budgettracker.screens.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.budgettracker.database.BudgetCategory
import com.example.android.budgettracker.database.BudgetDatabaseDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreateCategoryViewModel(
  private val budgetKey: Long = 0L,
  private val categoryKey: Long = 0L,
  val database: BudgetDatabaseDao) : ViewModel() {

  private val viewModelJob = Job()

  private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

  private val _navigateToCreateBudget = MutableLiveData<Long>()
  val navigateToCreateBudget : LiveData<Long>
    get() = _navigateToCreateBudget

  fun doneNavigating() {
    _navigateToCreateBudget.value = null
  }

  private var category = MutableLiveData<BudgetCategory?>()

  private val _newText = MutableLiveData<Boolean>()
  val newText : LiveData<Boolean>
    get() = _newText

  private val _name = MutableLiveData<String>()
  val name : LiveData<String>
    get() = _name

  private val _groupName = MutableLiveData<String>()
  val groupName : LiveData<String>
    get() = _groupName

  private val _allocation = MutableLiveData<String>()
  val allocation : LiveData<String>
    get() = _allocation

  private val _taxFree = MutableLiveData<Boolean>()
  val taxFree : LiveData<Boolean>
    get() = _taxFree

  private suspend fun getCategoryById(id: Long): BudgetCategory? {
    return withContext(Dispatchers.IO) {
      var category = database.getCategoryWithId(id)
      category
    }
  }

  private suspend fun insert(category: BudgetCategory) {
    withContext(Dispatchers.IO) {
      database.insertCategory(category)
    }
  }

  private suspend fun update(category: BudgetCategory) {
    withContext(Dispatchers.IO) {
      database.updateCategory(category)
    }
  }

  init {
    initializeCategory()
  }

  private fun initializeCategory() {
    uiScope.launch {
      if (categoryKey > 0) {
        _newText.value = false
        category.value = getCategoryById(categoryKey)
        _name.value = category.value?.name
        _groupName.value = category.value?.group
        _allocation.value = category.value?.budgeted.toString()
        _taxFree.value = category.value?.preTax
      } else {
        _newText.value = true
      }
    }
  }

  fun insertOrAddCategory(name: String, group: String, allocation: String, preTax: Boolean) {
    uiScope.launch {
      val newCategory = BudgetCategory()
      newCategory.budgetId = budgetKey
      newCategory.name = name
      newCategory.group = group
      newCategory.budgeted = allocation.toDouble()
      newCategory.preTax = preTax
      if (category.value == null) {
        insert(newCategory)
      } else {
        newCategory.categoryId = categoryKey
        update(newCategory)
      }

      println("testing: New Category")

      _navigateToCreateBudget.value = budgetKey

    }
  }

  fun cancel() {
    _navigateToCreateBudget.value = budgetKey
  }

  override fun onCleared() {
    super.onCleared()
    viewModelJob.cancel()
  }
}
