package com.example.android.budgettracker.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.ColumnInfo

@Dao
interface BudgetDatabaseDao {


  /** Budget */
  @Insert
  fun insertBudget(budget: Budget)

  @Update
  fun updateBudget(budget: Budget)

  @Query("DELETE FROM budget_table WHERE budgetId = :key")
  fun deleteBudget(key: Long)

  @Query("SELECT * FROM budget_table ORDER BY budgetId DESC LIMIT 1")
  fun getBudget(): Budget?

  @Query("SELECT name , budgetId FROM budget_table")
  fun getBudgetNames(): LiveData<List<NameTuple>>

  @Query("SELECT * FROM budget_table WHERE budgetId = :key")
  fun getBudgetWithId(key: Long): Budget?

  @Query("SELECT COUNT(budgetId) FROM budget_table")
  fun getBudgetCount(): Int

  @Query("DELETE FROM budget_table")
  fun clearBudget()

  /** Category */
  @Insert
  fun insertCategory(category: BudgetCategory)

  @Update
  fun updateCategory(category: BudgetCategory)

  @Query("DELETE FROM budget_category_table WHERE categoryId = :id")
  fun deleteCategory(id: Long)

  @Query("SELECT * FROM budget_category_table WHERE categoryId = :key")
  fun getCategoryWithId(key: Long): BudgetCategory?

  @Query("SELECT * FROM budget_category_table WHERE budgetId = :id ORDER BY `group`, name")
  fun getCategories(id: Long): LiveData<List<BudgetCategory>>

  @Query("SELECT categoryId, name, `group`, budgeted, ifnull(spent, 0) as spent FROM (SELECT categoryId, name, `group`, budgeted FROM budget_category_table WHERE budgetId = :id and `group` = :parentCategory ORDER BY name) as table1 LEFT JOIN (SELECT category, SUM(spent) as spent FROM budget_item_table WHERE budgetId = :id and month = :month and year = :year GROUP BY category) as table2 ON table1.name = table2.category")
  fun getHomeCategories(id : Long, month: String, year: Int, parentCategory: String): List<BudgetHomeCategory>

  @Query("SELECT name FROM budget_category_table WHERE budgetId = :id")
  fun getCategoryNames(id : Long): LiveData<List<String>>

  @Query("SELECT SUM(budgeted) FROM budget_category_table WHERE budgetId = :id and pre_tax = 1")
  fun getTotalCategories(id : Long): LiveData<Double?>

  @Query("SELECT SUM(budgeted) FROM budget_category_table WHERE budgetId = :id and pre_tax = 0")
  fun getTotalCategoriesTaxed(id : Long): LiveData<Double?>

  @Query("DELETE FROM budget_category_table WHERE budgetId = :id")
  fun clearCategories(id : Long)

  /** Item */
  @Insert
  fun insertItem(item: BudgetItem)

  @Update
  fun updateItem(item: BudgetItem)

  @Query("DELETE FROM budget_item_table WHERE id = :id")
  fun deleteItem(id: Long)

  @Query("SELECT * FROM budget_item_table WHERE id = :key")
  fun getItemWithId(key: Long): BudgetItem?

  @Query("SELECT * FROM budget_item_table WHERE budgetId = :id and category = :category and month = :month and year = :year ORDER BY day")
  fun getItemDetails(id: Long, category: String, month: String, year: Int): List<BudgetItem>

  @Query("DELETE FROM budget_item_table WHERE budgetId = :id")
  fun clearItems(id : Long)
}

class NameTuple {
  @ColumnInfo(name = "name")
  var name: String = ""

  @ColumnInfo(name = "budgetId")
  var budgetId: Long = 0L
}