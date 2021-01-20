package com.dyucorp.dyubomje.ui.start

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dyucorp.dyubomje.base.BaseViewModel

class StartViewModel : BaseViewModel() {

    val isClicked = MutableLiveData<Boolean>()

    fun onClickLoad() {
        isClicked.value = true
    }
}