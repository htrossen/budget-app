package com.example.android.budgettracker.database

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

data class BudgetHomeCategory(
  @PrimaryKey(autoGenerate = true)
  var categoryId: Long = 0L,

  @ColumnInfo(name = "name")
  var name: String = "",

  @ColumnInfo(name = "group")
  var group: String = "",

  @ColumnInfo(name = "budgeted")
  var budgeted: Double = 0.0,

  @ColumnInfo(name = "spent")
  var spent: Double = 0.0
)