package com.ramo.core.demo.base

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.ramo.core.SweetViewModel
import com.ramo.core.demo.customview.LoadingDialog
import com.ramo.core.ext.findGenericWithType
import com.ramo.core.state.NavEvent
import com.ramo.core.ui.activity.ViewBindingActivity

abstract class BaseActivity<VB : ViewBinding, VM : SweetViewModel> : ViewBindingActivity<VB>() {

    protected lateinit var viewModel: VM

    private val loadingDialog by lazy { LoadingDialog(this) }
    private val exceptionDialog by lazy {
        AlertDialog.Builder(this).apply {
            setTitle("Error")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        initViewModel()
        super.onCreate(savedInstanceState)
    }

    private fun initViewModel() {
        val vmClass = javaClass.findGenericWithType<VM>(1)
        viewModel = ViewModelProvider(this)[vmClass]
    }

    override fun sweetViewModel(): SweetViewModel? = viewModel

    override fun onChangeLoading(isLoading: Boolean) {
        if (isLoading) loadingDialog.show()
        else loadingDialog.dismiss()
    }

    override fun onAlert(stringId: Int, cancelable: Boolean) {
        Toast.makeText(this, getString(stringId), Toast.LENGTH_SHORT).show()
    }

    override fun onException(throwable: Throwable, cancelable: Boolean) {
        exceptionDialog.apply {
            setPositiveButton(null, null)
            setMessage(throwable.localizedMessage)
            setNegativeButton("Ok") { _, _ ->
                if (cancelable.not()) onBackPressed()
            }
        }.show()
    }

    override fun dismissAlertAndException() {
        exceptionDialog.create().dismiss()
    }

    override fun onNavigate(navEvent: NavEvent) {}
}