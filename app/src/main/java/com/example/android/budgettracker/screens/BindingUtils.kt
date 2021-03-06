package com.example.android.budgettracker.screens

import android.text.Html
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.android.budgettracker.database.BudgetCategory
import com.example.android.budgettracker.database.BudgetHomeCategory
import com.example.android.budgettracker.database.BudgetItem
import kotlin.math.round

@BindingAdapter("budgetCategory")
fun TextView.setBudgetCategory(item: BudgetCategory?) {
  item?.let {
    text = item.name
  }
}

@BindingAdapter("budgetedValue")
fun TextView.setBudgetedValue(item: BudgetCategory?) {
  item?.let {
    text = item.budgeted.toString()
  }
}

@BindingAdapter("homeHeader")
fun TextView.setHomeHeader(parentCategory: String?) {
  parentCategory?.let {
    text = parentCategory
  }
}

@BindingAdapter("homeCategory")
fun TextView.setHomeCategory(item: BudgetHomeCategory?) {
  item?.let {
    val category =  "<b>" + item.name + "</b> - \$" + item.spent + " of \$" + item.budgeted
    text = Html.fromHtml(category)
  }
}

@BindingAdapter("homeProgress")
fun ProgressBar.setHomeProgress(item: BudgetHomeCategory?) {
  item?.let{
    val progress = round((item.spent / item.budgeted) * 100).toInt()
    setProgress(progress)
  }
}

@BindingAdapter("budgetedTotalValue")
fun TextView.setBudgetedTotalValue(amount: Double?) {
  amount?.let {
    text = amount.toString()
  }
}

@BindingAdapter("itemDay")
fun TextView.setItemDay(item: BudgetItem?) {
  item?.let {
    text = item.day.toString()
  }
}

@BindingAdapter("itemDescription")
fun TextView.setItemDescription(item: BudgetItem?) {
  item?.let {
    text = item.description.toString()
  }
}

@BindingAdapter("itemSpent")
fun TextView.setItemSpent(item: BudgetItem?) {
  item?.let {
    text = item.spent.toString()
  }
}