package com.ramo.core.demo

import android.os.Bundle
import com.ramo.core.SweetViewModel
import com.ramo.core.demo.databinding.ActivityMainBinding
import com.ramo.core.state.NavEvent
import com.ramo.core.ui.activity.DataBindingActivity
import com.ramo.core.ui.activity.NormalActivity
import com.ramo.core.ui.activity.ViewBindingActivity

abstract class MainActivity : DataBindingActivity<ActivityMainBinding>(R.layout.activity_main){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun sweetViewModel(): SweetViewModel?  = null

    override fun onChangeLoading(isLoading: Boolean) {
    }

    override fun onAlert(stringId: Int, cancelable: Boolean) {
    }

    override fun onException(throwable: Throwable, cancelable: Boolean) {
    }

    override fun dismissAlertAndException() {
    }

    override fun onNavigate(navEvent: NavEvent) {
    }

}