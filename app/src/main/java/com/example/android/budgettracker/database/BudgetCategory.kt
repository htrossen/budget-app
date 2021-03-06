package com.example.android.budgettracker.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "budget_category_table")
data class BudgetCategory(
  @PrimaryKey(autoGenerate = true)
  var categoryId: Long = 0L,

  @ColumnInfo(name = "budgetId")
  var budgetId: Long = 0L,

  @ColumnInfo(name = "name")
  var name: String = "",

  @ColumnInfo(name = "group")
  var group: String = "",

  @ColumnInfo(name = "budgeted")
  var budgeted: Double = 0.0,

  @ColumnInfo(name = "pre_tax")
  var preTax: Boolean = false
)