package com.example.android.budgettracker.screens.item

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

import com.example.android.budgettracker.R
import com.example.android.budgettracker.database.BudgetDatabase
import com.example.android.budgettracker.databinding.ItemDetailListFragmentBinding

class ItemDetailListFragment : Fragment() {

  private lateinit var viewModel: ItemDetailListViewModel

  private lateinit var binding: ItemDetailListFragmentBinding

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    binding = DataBindingUtil.inflate(inflater, R.layout.item_detail_list_fragment, container, false)

    val application = requireNotNull(this.activity).application

    val arguments = ItemDetailListFragmentArgs.fromBundle(arguments!!)

    val dataSource = BudgetDatabase.getInstance(application).budgetDatabaseDao

    val viewModelFactory = ItemDetailListViewModelFactory(arguments.budgetKey, arguments.category, arguments.month, arguments.year, dataSource)

    viewModel = ViewModelProviders.of(this, viewModelFactory).get(ItemDetailListViewModel::class.java)

    binding.itemDetailListViewModel = viewModel
    binding.setLifecycleOwner(this)

    val adapter = ListItemDetailAdapter(ListItemDetailListener { itemId, type ->
      if (type == "edit") {
        viewModel.onItemClickedEdit(itemId)
      } else {
        viewModel.onItemClickedDelete(itemId)
      }
    })

    binding.itemList.adapter = adapter

    viewModel.items.observe(viewLifecycleOwner, Observer {
      it?.let {
        adapter.submitList(it)
      }
    })

    viewModel.navigateToCreateItem.observe(this, Observer { ids ->
      ids?.let {
        this.findNavController().navigate(ItemDetailListFragmentDirections.actionItemDetailListToCreateItem(ids.first, ids.second))
        viewModel.doneNavigating()
      }
    })

    return binding.root
  }
}
