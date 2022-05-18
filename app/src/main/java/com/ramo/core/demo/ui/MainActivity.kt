package com.ramo.core.demo.ui

import android.os.Bundle
import com.ramo.core.demo.base.BaseActivity
import com.ramo.core.demo.databinding.ActivityMainBinding
import com.ramo.core.ext.observeExt

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getData()
        binding.btnRefresh.setOnClickListener {
            viewModel.refreshData()
        }
        initObserver()
    }

    private fun initObserver() {
        observeExt(viewModel.data){
            binding.textView.text = it
        }
    }

    override fun onChangeLoading(isLoading: Boolean) {
        super.onChangeLoading(isLoading)
    }

}