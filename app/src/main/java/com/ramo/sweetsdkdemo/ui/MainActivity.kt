package com.ramo.sweetsdkdemo.ui

import android.os.Bundle
import com.ramo.sweetsdk.demo.databinding.ActivityMainBinding
import com.ramo.sweetsdk.ext.observeExt
import com.ramo.sweetsdkdemo.base.BaseActivity

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