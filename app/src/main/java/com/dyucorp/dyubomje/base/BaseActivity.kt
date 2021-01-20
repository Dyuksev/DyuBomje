package com.dyucorp.dyubomje.base

import android.os.Bundle
import android.os.PersistableBundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider

abstract class BaseActivity<V : BaseViewModel, B : ViewDataBinding> :
    AppCompatActivity() {

    protected abstract fun viewModelClass(): Class<V>
    protected lateinit var viewModel: V
    protected lateinit var binding: B

    @LayoutRes
    protected abstract fun layoutResId(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = initViewModel()
        binding = DataBindingUtil.setContentView(this, layoutResId())
        binding.lifecycleOwner = this
        binding.executePendingBindings()
    }

    private fun initViewModel() = ViewModelProvider(
        this,
        ViewModelProvider.NewInstanceFactory()
    ).get(viewModelClass())

}