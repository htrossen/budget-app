package com.example.android.budgettracker.screens.info

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer

import com.example.android.budgettracker.R
import com.example.android.budgettracker.databinding.InfoFragmentBinding

class InfoFragment : Fragment() {

  private lateinit var viewModel: InfoViewModel

  private lateinit var binding: InfoFragmentBinding

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
    binding = DataBindingUtil.inflate(inflater, R.layout.info_fragment, container, false)

    val arguments = InfoFragmentArgs.fromBundle(arguments!!)

    val viewModelFactory = InfoViewModelFactory(arguments.infoType)

    viewModel = ViewModelProviders.of(this, viewModelFactory).get(InfoViewModel::class.java)

    binding.infoViewModel = viewModel
    binding.setLifecycleOwner(this)

    viewModel.title.observe(this, Observer {
      it?.let {
        binding.infoTitle.text = resources.getString(it)
      }
    })

    viewModel.content.observe(this, Observer {
      it?.let {
        binding.infoContent.text = resources.getString(it)
      }
    })

    return binding.root
  }
}
