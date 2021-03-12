package com.example.android.budgettracker.screens.item

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

import com.example.android.budgettracker.R
import com.example.android.budgettracker.database.BudgetDatabase
import com.example.android.budgettracker.databinding.CreateItemFragmentBinding

class CreateItemFragment : Fragment() {
  private lateinit var viewModel: CreateItemViewModel

  private lateinit var binding: CreateItemFragmentBinding

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    binding = DataBindingUtil.inflate(inflater, R.layout.create_item_fragment, container, false)

    val application = requireNotNull(this.activity).application

    val arguments = CreateItemFragmentArgs.fromBundle(arguments!!)

    val dataSource = BudgetDatabase.getInstance(application).budgetDatabaseDao

    val viewModelFactory = CreateItemViewModelFactory(arguments.budgetKey, arguments.itemKey, dataSource)

    viewModel = ViewModelProviders.of(this, viewModelFactory).get(CreateItemViewModel::class.java)

    binding.createItemViewModel = viewModel
    binding.setLifecycleOwner(this)

    viewModel.navigateToHome.observe(this, Observer { budgetId ->
      budgetId?.let {
        this.findNavController().navigate(CreateItemFragmentDirections.actionCreateItemToHome(budgetId))
        viewModel.doneNavigating()
      }
    })

    viewModel.newText.observe(this, Observer {
      it?.let {
        if (it) {
          binding.itemCreateNewText.text = resources.getString(R.string.create_item)
          binding.itemAddButton.text = resources.getString(R.string.add_expense)
        } else {
          binding.itemCreateNewText.text = resources.getString(R.string.edit_item)
          binding.itemAddButton.text = resources.getString(R.string.edit_expense)
        }
      }
    })

    viewModel.month.observe(this, Observer {
      it?.let {
        binding.itemMonth.setSelection(it)
      }
    })

    viewModel.day.observe(this, Observer {
      it?.let {
        binding.itemDay.setSelection(it)
      }
    })

    viewModel.category.observe(this, Observer { category ->
      category?.let {
        if (viewModel.categories.value != null) {
          binding.itemCategory.setSelection(viewModel.categories.value!!.indexOf(category))
        }
      }
    })

    viewModel.categoryNames.observe(this, Observer {
      it?.let {
        viewModel.categoryListUpdate()

      }
    })

    viewModel.categories.observe(this, Observer {
      it?.let {
        val adapter = ArrayAdapter(this.context!!, android.R.layout.simple_spinner_item, it.toMutableList())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.itemCategory.adapter = adapter

        if (viewModel.category.value != null) {
          binding.itemCategory.setSelection(it.indexOf(viewModel.category.value!!))
        }
      }
    })

    return binding.root
  }
}
