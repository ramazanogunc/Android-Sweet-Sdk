package com.ramo.sweetsdk.demo.base

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.ramo.sweetsdk.SweetViewModel
import com.ramo.sweetsdk.demo.customview.LoadingDialog
import com.ramo.sweetsdk.ext.findGenericWithType
import com.ramo.sweetsdk.ext.safeContext
import com.ramo.sweetsdk.state.NavEvent
import com.ramo.sweetsdk.ui.fragment.ViewBindingFragment

abstract class BaseFragment<VB : ViewBinding, VM : SweetViewModel> : ViewBindingFragment<VB>() {

    protected lateinit var viewModel: VM
    protected open fun isSharedViewModel(): Boolean = false

    val loadingDialog by lazy { LoadingDialog(requireContext()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViewModel()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initViewModel() {
        val vmClass = javaClass.findGenericWithType<VM>(1)
        viewModel = ViewModelProvider(
            if (isSharedViewModel()) requireActivity() else this
        )[vmClass]
    }

    override fun sweetViewModel(): SweetViewModel? = viewModel

    override fun onChangeLoading(isLoading: Boolean) {
        if (isLoading) loadingDialog.show()
        else loadingDialog.dismiss()
    }

    override fun onAlert(stringId: Int, cancelable: Boolean) {
        safeContext {
            Toast.makeText(it, getString(stringId), Toast.LENGTH_LONG).show()
        }

    }

    override fun onException(throwable: Throwable, cancelable: Boolean) {
        TODO("Not yet implemented")
    }

    override fun dismissAlertAndException() {
        TODO("Not yet implemented")
    }

    override fun onNavigate(navEvent: NavEvent) {
        TODO("Not yet implemented")
    }
}