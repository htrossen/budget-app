package com.example.android.budgettracker.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "budget_table")
data class Budget(
  @PrimaryKey(autoGenerate = true)
  var budgetId: Long = 0L,

  @ColumnInfo(name = "name")
  var name: String = "Budget",

  @ColumnInfo(name = "salary")
  var salary: Double = 0.0,

  @ColumnInfo(name = "tax")
  var tax: Double = 0.0
)