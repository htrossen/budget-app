package com.example.android.budgettracker.screens.title

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.android.budgettracker.R
import com.example.android.budgettracker.database.BudgetDatabase
import com.example.android.budgettracker.databinding.TitleFragmentBinding

/**
 * Fragment for the starting or title screen of the app
 */
class TitleFragment : Fragment() {
  private lateinit var viewModel: TitleViewModel

  private lateinit var binding: TitleFragmentBinding

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?): View {
    // Inflate the layout for this fragment
    binding = DataBindingUtil.inflate(inflater, R.layout.title_fragment, container, false)

    val application = requireNotNull(this.activity).application

    val dataSource = BudgetDatabase.getInstance(application).budgetDatabaseDao

    val viewModelFactory = TitleViewModelFactory(dataSource)

    viewModel = ViewModelProviders.of(this, viewModelFactory).get(TitleViewModel::class.java)

    binding.titleViewModel = viewModel

    binding.buttonCreateBudget.setOnClickListener {
      if (viewModel.count.value != null && viewModel.count.value != 0) {
        findNavController().navigate(TitleFragmentDirections.actionTitleToHome(1))
      } else {
        findNavController().navigate(TitleFragmentDirections.actionTitleToCreateBudget(0))
      }
    }

    return binding.root
  }
}