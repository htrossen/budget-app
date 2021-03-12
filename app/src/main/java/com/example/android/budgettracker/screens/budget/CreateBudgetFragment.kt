package com.example.android.budgettracker.screens.budget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.android.budgettracker.R
import com.example.android.budgettracker.database.BudgetDatabase
import com.example.android.budgettracker.databinding.CreateBudgetFragmentBinding

class CreateBudgetFragment : Fragment() {

  private lateinit var viewModel: CreateBudgetViewModel

  private lateinit var binding: CreateBudgetFragmentBinding

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    binding = DataBindingUtil.inflate(inflater, R.layout.create_budget_fragment, container, false)

    val application = requireNotNull(this.activity).application

    val dataSource = BudgetDatabase.getInstance(application).budgetDatabaseDao

    val arguments = CreateBudgetFragmentArgs.fromBundle(arguments!!)

    val viewModelFactory = CreateBudgetViewModelFactory(arguments.budgetKey, dataSource, application)

    viewModel = ViewModelProviders.of(this, viewModelFactory).get(CreateBudgetViewModel::class.java)

    binding.createBudgetViewModel = viewModel

    val adapter = BudgetCategoryAdapter(BudgetCategoryListener { categoryId, type ->
      if (type == "edit") {
        viewModel.onCategoryClickedEdit(categoryId)
      } else {
        viewModel.onCategoryClickedDelete(categoryId)
      }
    })

    binding.budgetCategoriesList.adapter = adapter


    viewModel.navigateToCreateCategory.observe(this, Observer { ids ->
      ids?.let {
        this.findNavController().navigate(CreateBudgetFragmentDirections.actionCreateBudgetToCreateCategory(ids.first, ids.second))
        viewModel.doneNavigating()
      }
    })

    viewModel.navigateToHome.observe(this, Observer { budgetId ->
      budgetId?.let {
        this.findNavController().navigate(CreateBudgetFragmentDirections.actionCreateBudgetToHome(budgetId))
        viewModel.doneNavigating()
      }
    })

    viewModel.title.observe(this, Observer {
      it?.let {
        binding.budgetCreateNewText.text = resources.getString(it)
      }
    })

    viewModel.categories.observe(viewLifecycleOwner, Observer {
      it?.let {
        adapter.addHeaderAndSubmitList(it, viewModel.available.value)
      }
    })

    viewModel.totalAllocated.observe(viewLifecycleOwner, Observer {
      it?.let {
        viewModel.updateAvailable()
      }
    })

    viewModel.totalAllocatedTaxed.observe(viewLifecycleOwner, Observer {
      it?.let {
        viewModel.updateAvailable()
      }
    })

    viewModel.available.observe(viewLifecycleOwner, Observer {
      it?.let {
        adapter.addHeaderAndSubmitList(viewModel.categories.value, it)
      }
    })

    binding.setLifecycleOwner(this)

    return binding.root
  }

}
