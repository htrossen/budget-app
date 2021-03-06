package com.example.android.budgettracker.screens.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.budgettracker.database.BudgetHomeCategory
import com.example.android.budgettracker.databinding.HomeHeaderBinding
import com.example.android.budgettracker.databinding.ListItemHomeCategoryBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private val ITEM_VIEW_TYPE_HEADER = 0
private val ITEM_VIEW_TYPE_ITEM = 1

class HomeCategoryAdapter(val clickListener: BudgetHomeCategoryListener) : ListAdapter<HomeDataItem,
  RecyclerView.ViewHolder>(BudgetHomeCategoryDiffCallback()) {

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
      is HomeDataItem.Header -> ITEM_VIEW_TYPE_HEADER
      is HomeDataItem.BudgetHomeCategoryItem -> ITEM_VIEW_TYPE_ITEM
    }
  }

  fun addHeaderAndSubmitList(list: List<BudgetHomeCategory>?, patentCategory: String) {
    adapterScope.launch {
      val items = when (list) {
        null -> listOf(HomeDataItem.Header(patentCategory))
        else -> listOf(HomeDataItem.Header(patentCategory)) + list.map {HomeDataItem.BudgetHomeCategoryItem(it)}
      }
      withContext(Dispatchers.Main) {
        submitList(items)
      }
    }
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    when (holder) {
      is ViewHolderList -> {
        val categoryItem = getItem(position) as HomeDataItem.BudgetHomeCategoryItem
        holder.bind(clickListener, categoryItem.category)
      }
      is ViewHolderHeader -> {
        val header = getItem(position) as HomeDataItem.Header
        holder.bind(header.category)
      }
    }
  }

  class ViewHolderList private constructor(val binding: ListItemHomeCategoryBinding)
    : RecyclerView.ViewHolder(binding.root) {

    fun bind(clickListener: BudgetHomeCategoryListener, item: BudgetHomeCategory) {
      binding.category = item
      binding.clickListener = clickListener
      binding.executePendingBindings()
    }

    companion object {
      fun from(parent: ViewGroup): ViewHolderList {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemHomeCategoryBinding.inflate(layoutInflater, parent, false)

        return ViewHolderList(binding)
      }
    }
  }

  class ViewHolderHeader private constructor(val binding: HomeHeaderBinding)
    : RecyclerView.ViewHolder(binding.root) {

    fun bind(patentCategory: String) {
      binding.homeHeaderCategory = patentCategory
      binding.executePendingBindings()
    }

    companion object {
      fun from(parent: ViewGroup): ViewHolderHeader {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = HomeHeaderBinding.inflate(layoutInflater, parent, false)
        return ViewHolderHeader(binding)
      }
    }
  }
}

class BudgetHomeCategoryDiffCallback : DiffUtil.ItemCallback<HomeDataItem>() {
  override fun areItemsTheSame(oldItem: HomeDataItem, newItem: HomeDataItem): Boolean {
    return oldItem.id == newItem.id
  }

  override fun areContentsTheSame(oldItem: HomeDataItem, newItem: HomeDataItem): Boolean {
    return oldItem == newItem
  }
}

class BudgetHomeCategoryListener(val clickListener: (name: String) -> Unit) {
  fun onClick(category: BudgetHomeCategory) = clickListener(category.name)
}

sealed class HomeDataItem {
  data class BudgetHomeCategoryItem(val category: BudgetHomeCategory): HomeDataItem() {
    override val id = category.categoryId
  }

  data class Header(val category: String): HomeDataItem() {
    override val id = Long.MIN_VALUE
  }

  abstract val id: Long
}