package com.dyucorp.dyubomje

import androidx.navigation.NavController
import com.dyucorp.dyubomje.base.BaseActivity
import com.dyucorp.dyubomje.databinding.ActivityMainBinding

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {

    lateinit var navController: NavController

    override fun viewModelClass(): Class<MainViewModel> = MainViewModel::class.java

    override fun layoutResId(): Int = R.layout.activity_main
}