package com.example.android.budgettracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.android.budgettracker.database.BudgetDatabase
import com.example.android.budgettracker.databinding.ActivityMainBinding
import com.example.android.budgettracker.screens.title.TitleFragmentDirections
import com.example.android.budgettracker.screens.title.TitleViewModel
import com.example.android.budgettracker.screens.title.TitleViewModelFactory

class MainActivity : AppCompatActivity() {

  lateinit var binding: ActivityMainBinding

  lateinit var viewModel: TitleViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    setupNavigation()

    val application = requireNotNull(this).application

    val dataSource = BudgetDatabase.getInstance(application).budgetDatabaseDao

    val viewModelFactory = TitleViewModelFactory(dataSource)

    viewModel = ViewModelProviders.of(this, viewModelFactory).get(TitleViewModel::class.java)

    binding.titleViewModel = viewModel


    viewModel.names.observe(this, Observer { names ->
      if (binding.navigationView.menu.getItem(0).hasSubMenu()) {
        binding.navigationView.menu.getItem(0).subMenu.clear()
        names.forEach {
          binding.navigationView.menu.getItem(0).subMenu.add(it.name)
        }
      }
      binding.navigationView.invalidate()
    })
  }

  override fun onSupportNavigateUp()
    = navigateUp(findNavController(R.id.nav_host_fragment), binding.drawerLayout)

  private fun setupNavigation() {

    // first find the nav controller
    val navController = findNavController(R.id.nav_host_fragment)

    setSupportActionBar(binding.toolbar)

    // then setup the action bar, tell it about the DrawerLayout
    setupActionBarWithNavController(navController, binding.drawerLayout)


    // finally setup the left drawer (called a NavigationView)
    binding.navigationView.setupWithNavController(navController)
    binding.navigationView.setNavigationItemSelectedListener {
      println("testing: " + it.title)
      if (it.title !in listOf("Create New Budget", "About", "FAQ")) {
        val index = viewModel.names.value!!.indexOfFirst { pair -> pair.name == it.title }
        println("testing: " + index)
        navController.navigate(TitleFragmentDirections.actionTitleToHome(viewModel.names.value!![index].budgetId))
      } else {
        when (it.title) {
          "Create New Budget" -> navController.navigate(TitleFragmentDirections.actionTitleToCreateBudget(0L))
          "About" -> navController.navigate(TitleFragmentDirections.actionTitleToInfo("About"))
          "FAQ" -> navController.navigate(TitleFragmentDirections.actionTitleToInfo("FAQ"))
          else -> println("testing: TODO " + it.title)
        }
      }
      binding.drawerLayout.closeDrawer(GravityCompat.START)
      true
    }

    navController.addOnDestinationChangedListener { _, destination: NavDestination, _ ->
      val toolBar = supportActionBar ?: return@addOnDestinationChangedListener
      when(destination.id) {
        R.id.title_destination -> {
          toolBar.setDisplayShowTitleEnabled(true)
        }
        else -> {
          toolBar.setDisplayShowTitleEnabled(true)
        }
      }
    }
  }
}
