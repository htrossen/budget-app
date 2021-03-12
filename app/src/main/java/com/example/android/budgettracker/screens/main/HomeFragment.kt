package com.example.android.budgettracker.screens.main

import android.app.Dialog
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

import com.example.android.budgettracker.R
import com.example.android.budgettracker.database.BudgetDatabase
import com.example.android.budgettracker.databinding.HomeFragmentBinding
import org.threeten.bp.Month
import java.util.Calendar
import android.widget.LinearLayout
import android.widget.DatePicker

class HomeFragment : Fragment() {

  private lateinit var viewModel: HomeViewModel

  private lateinit var binding: HomeFragmentBinding

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    binding = DataBindingUtil.inflate(inflater, R.layout.home_fragment, container, false)

    val application = requireNotNull(this.activity).application

    val arguments = HomeFragmentArgs.fromBundle(arguments!!)

    val dataSource = BudgetDatabase.getInstance(application).budgetDatabaseDao

    val viewModelFactory = HomeViewModelFactory(arguments.budgetKey, dataSource)

    viewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeViewModel::class.java)

    binding.homeViewModel = viewModel

    viewModel.name.observe(this, Observer {
      binding.name.text = it
    })

    viewModel.date.observe(this, Observer {
      binding.date.text = it
    })

    val needsAdapter = HomeCategoryAdapter(BudgetHomeCategoryListener { categoryName ->
      val month = viewModel.month.value ?: ""
      val year = viewModel.year.value ?: 0
      this.findNavController().navigate(HomeFragmentDirections.actionHomeToItemDetailList(arguments.budgetKey, categoryName, month, year))
    })

    binding.needsList.adapter = needsAdapter

    val savingsAdapter = HomeCategoryAdapter(BudgetHomeCategoryListener { categoryName ->
      val month = viewModel.month.value ?: ""
      val year = viewModel.year.value ?: 0
      this.findNavController().navigate(HomeFragmentDirections.actionHomeToItemDetailList(arguments.budgetKey, categoryName, month, year))
    })

    binding.savingsList.adapter = savingsAdapter

    val wantsAdapter = HomeCategoryAdapter(BudgetHomeCategoryListener { categoryName ->
      val month = viewModel.month.value ?: ""
      val year = viewModel.year.value ?: 0
      this.findNavController().navigate(HomeFragmentDirections.actionHomeToItemDetailList(arguments.budgetKey, categoryName, month, year))
    })

    binding.wantsList.adapter = wantsAdapter

    viewModel.month.observe(this, Observer {
      it?.let {
        println("testing: " + it)
        viewModel.categoriesUpdate()
      }
    })

    viewModel.year.observe(this, Observer {
      it?.let {
        println("testing: " + it)
        viewModel.categoriesUpdate()
      }
    })

    viewModel.needsCategories.observe(this, Observer {
      it?.let {
        needsAdapter.addHeaderAndSubmitList(it, "Needs")
      }
    })

    viewModel.savingsCategories.observe(this, Observer {
      it?.let {
        savingsAdapter.addHeaderAndSubmitList(it, "Savings")
      }
    })

    viewModel.wantsCategories.observe(this, Observer {
      it?.let {
        wantsAdapter.addHeaderAndSubmitList(it, "Wants")
      }
    })

    viewModel.changeDate.observe(this, Observer {
      if (it) {
        val  dialog = Dialog(this.context!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_date_picker)
        dialog.show()
        val c = Calendar.getInstance()
        val datePicker = dialog.findViewById(R.id.date_picker) as DatePicker
        val dateTimeSet = dialog.findViewById(R.id.date_time_set) as Button

        datePicker.init(
          viewModel.year.value ?: c.get(Calendar.YEAR),
          Month.valueOf(viewModel.month.value?.toUpperCase() ?: Month.of(c.get(Calendar.MONTH)).name).value - 1,
          c.get(Calendar.DAY_OF_MONTH),
          null
        )

        val ll = datePicker.getChildAt(0) as LinearLayout
        val ll2 = ll.getChildAt(0) as LinearLayout
        ll2.getChildAt(1).visibility = View.INVISIBLE

        dateTimeSet.setOnClickListener {
          dialog.dismiss()
          viewModel.onUpdateDate(Month.of(datePicker.month + 1).name.toLowerCase().capitalize(), datePicker.year)
        }
      }
    })

    viewModel.navigateToCreateItem.observe(this, Observer { budgetId ->
      budgetId?.let {
        this.findNavController().navigate(HomeFragmentDirections.actionHomeToCreateItem(budgetId))
        viewModel.doneNavigating()
      }
    })

    viewModel.edit.observe(this, Observer {
      if (it == true) {
        println("testing: edit my budget " + arguments.budgetKey)

        this.findNavController().navigate(HomeFragmentDirections.actionHomeToCreateBudget(arguments.budgetKey))
        viewModel.doneNavigating()
      }
    })

    viewModel.delete.observe(this, Observer {
      if (it == true) {
        println("testing: delete my budget " + arguments.budgetKey)

        this.findNavController().navigate(HomeFragmentDirections.actionHomeToTitle())
        viewModel.doneNavigating()
      }
    })


    setHasOptionsMenu(true)

    return binding.root
  }

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    inflater.inflate(R.menu.home_options, menu)
    super.onCreateOptionsMenu(menu, inflater)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
      when (item.itemId) {
        R.id.edit_budget -> viewModel.editBudget()
        R.id.delete_budget -> viewModel.deleteBudget()
      }
    return super.onOptionsItemSelected(item)
  }
}
