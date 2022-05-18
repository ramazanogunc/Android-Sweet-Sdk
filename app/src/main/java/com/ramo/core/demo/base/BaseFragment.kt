package com.ramo.core.demo.base

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.ramo.core.SweetViewModel
import com.ramo.core.demo.customview.LoadingDialog
import com.ramo.core.ext.findGenericWithType
import com.ramo.core.ext.safeContext
import com.ramo.core.state.NavEvent
import com.ramo.core.ui.fragment.ViewBindingFragment

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