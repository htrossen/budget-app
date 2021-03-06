package com.example.android.budgettracker.screens.item

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.budgettracker.database.BudgetItem
import com.example.android.budgettracker.databinding.ListItemDetailBinding

class ListItemDetailAdapter(val clickListener: ListItemDetailListener) : ListAdapter<BudgetItem,
  ListItemDetailAdapter.ViewHolder>(ListItemDetailDiffCallback()) {

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val item = getItem(position)

    holder.bind(clickListener, item)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return ViewHolder.from(parent)
  }

  class ViewHolder private constructor(val binding: ListItemDetailBinding)
    : RecyclerView.ViewHolder(binding.root) {

    fun bind(clickListener: ListItemDetailListener, item: BudgetItem) {
      binding.item = item
      binding.clickListener = clickListener
      binding.executePendingBindings()
    }

    companion object {
      fun from(parent: ViewGroup): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemDetailBinding.inflate(layoutInflater, parent, false)

        return ViewHolder(binding)
      }
    }
  }
}

class ListItemDetailDiffCallback : DiffUtil.ItemCallback<BudgetItem>() {
  override fun areItemsTheSame(oldItem: BudgetItem, newItem: BudgetItem): Boolean {
    return oldItem.id == newItem.id
  }

  override fun areContentsTheSame(oldItem: BudgetItem, newItem: BudgetItem): Boolean {
    return oldItem == newItem
  }
}

class ListItemDetailListener(val clickListener: (itemId: Long, type: String) -> Unit) {
  fun onEdit(item: BudgetItem) = clickListener(item.id, "edit")
  fun onDelete(item: BudgetItem) = clickListener(item.id, "delete")
}
