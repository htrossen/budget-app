package com.example.android.budgettracker.screens.budget

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.budgettracker.database.BudgetCategory
import com.example.android.budgettracker.databinding.HeaderBinding
import com.example.android.budgettracker.databinding.ListItemBudgetCategoryBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private val ITEM_VIEW_TYPE_HEADER = 0
private val ITEM_VIEW_TYPE_ITEM = 1

class BudgetCategoryAdapter(val clickListener: BudgetCategoryListener) : ListAdapter<DataItem,
  RecyclerView.ViewHolder>(BudgetCategoryDiffCallback()) {

  private val adapterScope = CoroutineScope((Dispatchers.Default))

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    return when (viewType) {
      ITEM_VIEW_TYPE_HEADER -> ViewHolderHeader.from(parent)
      ITEM_VIEW_TYPE_ITEM -> ViewHolderList.from(parent)
      else -> throw ClassCastException("Unknown viewType $viewType")
    }
  }

  override fun getItemViewType(position: Int): Int {
    return when (getItem(position)) {
      is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
      is DataItem.BudgetCategoryItem -> ITEM_VIEW_TYPE_ITEM
    }
  }

  fun addHeaderAndSubmitList(list: List<BudgetCategory>?, amount: Double?) {
    adapterScope.launch {
      val items = when (list) {
        null -> listOf(DataItem.Header(0.00))
        else -> listOf(DataItem.Header(amount ?: 0.00)) + list.map {DataItem.BudgetCategoryItem(it)}
      }
      withContext(Dispatchers.Main) {
        submitList(items)
      }
    }
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    when (holder) {
      is ViewHolderList -> {
        val categoryItem = getItem(position) as DataItem.BudgetCategoryItem
        holder.bind(clickListener, categoryItem.category)
      }
      is ViewHolderHeader -> {
        val header = getItem(position) as DataItem.Header
        holder.bind(header.amount)
      }
    }
  }

  class ViewHolderList private constructor(val binding: ListItemBudgetCategoryBinding)
    : RecyclerView.ViewHolder(binding.root) {

    fun bind(clickListener: BudgetCategoryListener, item: BudgetCategory) {
      binding.category = item
      binding.clickListener = clickListener
      binding.executePendingBindings()
    }

    companion object {
      fun from(parent: ViewGroup): ViewHolderList {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemBudgetCategoryBinding.inflate(layoutInflater, parent, false)
        return ViewHolderList(binding)
      }
    }
  }

  class ViewHolderHeader private constructor(val binding: HeaderBinding)
    : RecyclerView.ViewHolder(binding.root) {

    fun bind(amount: Double) {
      binding.categoriesTotal = amount
      binding.executePendingBindings()
    }

    companion object {
      fun from(parent: ViewGroup): ViewHolderHeader {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = HeaderBinding.inflate(layoutInflater, parent, false)
        return ViewHolderHeader(binding)
      }
    }
  }
}

class BudgetCategoryDiffCallback : DiffUtil.ItemCallback<DataItem>() {
  override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
    return oldItem.id == newItem.id
  }

  override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
    return oldItem == newItem
  }
}

class BudgetCategoryListener(val clickListener: (categoryId: Long, type: String) -> Unit) {
  fun onEdit(category: BudgetCategory) = clickListener(category.categoryId, "edit")
  fun onDelete(category: BudgetCategory) = clickListener(category.categoryId, "delete")
}

sealed class DataItem {
  data class BudgetCategoryItem(val category: BudgetCategory): DataItem() {
    override val id = category.categoryId
  }

  data class Header(val amount: Double): DataItem() {
    override val id = Long.MIN_VALUE
  }

  abstract val id: Long
}