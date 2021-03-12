package com.example.android.budgettracker.screens.budget

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.budgettracker.R
import com.example.android.budgettracker.database.Budget
import com.example.android.budgettracker.database.BudgetDatabaseDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.round

class CreateBudgetViewModel(private val budgetKey: Long = 0L, val database: BudgetDatabaseDao,
  application: Application) : AndroidViewModel(application) {

  private var viewModelJob = Job()

  private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

  private val _navigateToCreateCategory = MutableLiveData<Pair<Long,Long>>()
  val navigateToCreateCategory : LiveData<Pair<Long,Long>>
    get() = _navigateToCreateCategory

  private val _navigateToHome = MutableLiveData<Long>()
  val navigateToHome : LiveData<Long>
    get() = _navigateToHome

  fun doneNavigating() {
    _navigateToCreateCategory.value = null
    _navigateToHome.value = null
  }

  private var budget = MutableLiveData<Budget?>()

  private var newBudgetKey = MutableLiveData<Long?>()

  private val _title = MutableLiveData<Int>()
  val title : LiveData<Int>
    get() = _title

  private val _name = MutableLiveData<String>()
  val name : LiveData<String>
    get() = _name

  private val _salary = MutableLiveData<String>()
  val salary : LiveData<String>
    get() = _salary

  private val _tax = MutableLiveData<String>()
  val tax : LiveData<String>
    get() = _tax

  private val _available = MutableLiveData<Double>()
  val available : LiveData<Double>
    get() = _available

  val categories = database.getCategories(budgetKey)

  val totalAllocated = database.getTotalCategories(budgetKey)

  val totalAllocatedTaxed = database.getTotalCategoriesTaxed(budgetKey)

  private suspend fun getBudget(): Budget? {
    return withContext(Dispatchers.IO) {
      var budget = database.getBudget()
      budget
    }
  }

  private suspend fun getBudgetById(id: Long): Budget? {
    return withContext(Dispatchers.IO) {
      var budget = database.getBudgetWithId(id)
      budget
    }
  }

  private suspend fun insert(budget: Budget) {
    withContext(Dispatchers.IO) {
      database.insertBudget(budget)
    }
  }

  private suspend fun update(budget: Budget) {
    withContext(Dispatchers.IO) {
      database.updateBudget(budget)
    }
  }

  private suspend fun count(): Int {
    return withContext(Dispatchers.IO) {
      val count = database.getBudgetCount()
      count
    }
  }

  private suspend fun deleteCategory(categoryId: Long) {
    withContext(Dispatchers.IO) {
      database.deleteCategory(categoryId)
    }
  }

  private suspend fun clear() {
    withContext(Dispatchers.IO) {
      database.clearBudget()
    }
  }

  fun onCategoryClickedEdit(categoryId: Long) {
    if (budgetKey != 0L) {
      _navigateToCreateCategory.value = Pair(budgetKey, categoryId)
    } else {
      _navigateToCreateCategory.value = Pair(newBudgetKey.value ?: 0L, categoryId)
    }
    println("testing: category clicked edit")
  }

  fun onCategoryClickedDelete(categoryId: Long) {
    uiScope.launch {
      deleteCategory(categoryId)
    }
    println("testing: category clicked delete")
  }

  fun updateAvailable() {
    if (_salary.value != null) {
      val grossSalary = salary.value?.toDouble() ?: 0.0
      val tax = 1 - (_tax.value?.toDouble() ?: 0.0)
      val allocated = totalAllocated.value ?: 0.0
      val allocatedTaxed = totalAllocatedTaxed.value?: 0.0
      _available.value = round(((grossSalary.div(12) - allocated) * tax - allocatedTaxed) * 100) / 100
    } else {
      _available.value = 0.0
    }
  }

  fun onAddCategory(name: String, salary: String, tax: String) {
    insertOrUpdateBudget(name, salary, tax)
    println("testing: " + budget.value)
    println("testing: " + budgetKey)
    if (budgetKey != 0L) {
      _navigateToCreateCategory.value = Pair(budgetKey, 0L)
    } else {
      _navigateToCreateCategory.value = Pair(newBudgetKey.value ?: 0L, 0L)
    }
  }

  fun onDone(name: String, salary: String, tax: String) {
    insertOrUpdateBudget(name, salary, tax)

    if (budgetKey != 0L) {
      _navigateToHome.value = budgetKey
    } else {
      _navigateToHome.value = newBudgetKey.value
    }

  }

  init {
    initializeBudget()
  }

  private fun initializeBudget() {
    uiScope.launch {
      if (budgetKey > 0) {
        _title.value = R.string.edit_budget
        budget.value = getBudgetById(budgetKey)
        _name.value = budget.value?.name
        _salary.value = budget.value?.salary.toString()
        _tax.value = budget.value?.tax.toString()
        updateAvailable()
      } else {
        _title.value = R.string.create_new_budget
        newBudgetKey.value = (count() + 1).toLong()
        _available.value = 0.0
      }
    }
  }

  private fun insertOrUpdateBudget(name: String, salary: String, tax: String) {
    val newBudget = Budget()
    newBudget.name = name
    newBudget.salary = salary.toDouble()
    newBudget.tax = tax.toDouble()
    uiScope.launch {
      if (budget.value == null) {
        insert(newBudget)
      } else {
        newBudget.budgetId = budgetKey
        update(newBudget)
      }
      budget.value = getBudget()
    }
  }

  override fun onCleared() {
    super.onCleared()
    viewModelJob.cancel()
  }
}
