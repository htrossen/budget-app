package com.example.android.budgettracker.screens.info

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.budgettracker.R
import kotlinx.coroutines.Job

class InfoViewModel(private val infoType: String = "") : ViewModel() {
  private val viewModelJob = Job()

  private val _title = MutableLiveData<Int>()
  val title : LiveData<Int>
    get() = _title

  private val _content = MutableLiveData<Int>()
  val content : LiveData<Int>
    get() = _content

  init {
    initializeInfo()
  }

  private fun initializeInfo() {
    if (infoType == "About") {
      _title.value = R.string.about
      _content.value = R.string.about_content
    } else {
      _title.value = R.string.faq
      _content.value = R.string.faq_content
    }
  }

  override fun onCleared() {
    super.onCleared()
    viewModelJob.cancel()
  }
}
