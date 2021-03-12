package com.example.android.budgettracker.screens.category

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

import com.example.android.budgettracker.R
import com.example.android.budgettracker.database.BudgetDatabase
import com.example.android.budgettracker.databinding.CreateCategoryFragmentBinding

class CreateCategoryFragment : Fragment() {

  private lateinit var viewModel: CreateCategoryViewModel

  private lateinit var binding: CreateCategoryFragmentBinding

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    binding = DataBindingUtil.inflate(inflater, R.layout.create_category_fragment, container, false)

    val application = requireNotNull(this.activity).application

    val arguments = CreateCategoryFragmentArgs.fromBundle(arguments!!)

    val dataSource = BudgetDatabase.getInstance(application).budgetDatabaseDao
    val viewModelFactory = CreateCategoryViewModelFactory(arguments.budgetKey, arguments.categoryKey, dataSource)

    viewModel = ViewModelProviders.of(this, viewModelFactory).get(CreateCategoryViewModel::class.java)

    binding.createCategoryViewModel = viewModel
    binding.setLifecycleOwner(this)

    viewModel.navigateToCreateBudget.observe(this, Observer { budgetId ->
      budgetId?.let {
        this.findNavController().navigate(CreateCategoryFragmentDirections.actionCreateCategoryToCreateBudget(budgetId))
        viewModel.doneNavigating()
      }
    })

    viewModel.newText.observe(this, Observer {
      it?.let {
        if (it) {
          binding.categoryCreateNewText.text = resources.getString(R.string.create_new_category)
          binding.categoryAddCategoryButton.text = resources.getString(R.string.add_category)
        } else {
          binding.categoryCreateNewText.text = resources.getString(R.string.edit_new_category)
          binding.categoryAddCategoryButton.text = resources.getString(R.string.edit_category)
        }
      }
    })

    viewModel.groupName.observe(this, Observer { groupName ->
      groupName?.let {
        val groups = resources.getStringArray(R.array.budget_groups)
        binding.categoryGroup.setSelection(groups.indexOf(groupName))
      }
    })

    return binding.root
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      else -> viewModel.cancel()
    }
    return true
  }
}
