package com.example.android.budgettracker.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "budget_item_table")
data class BudgetItem(
  @PrimaryKey(autoGenerate = true)
  var id: Long = 0L,

  @ColumnInfo(name = "budgetId")
  var budgetId: Long = 0L,

  @ColumnInfo(name = "category")
  var category: String = "",

  @ColumnInfo(name = "spent")
  var spent: Double = 0.0,

  @ColumnInfo(name = "cost")
  var cost: Double = 0.0,

  @ColumnInfo(name = "split")
  var split: Int = 1,

  @ColumnInfo(name = "description")
  var description: String = "",

  @ColumnInfo(name = "month")
  var month: String = "January",

  @ColumnInfo(name = "day")
  var day: Int = 1,

  @ColumnInfo(name = "year")
  var year: Int = 2019
)