package com.ramo.sweetsdk.ui.activity

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.ramo.sweetsdk.SweetViewModel
import com.ramo.sweetsdk.ext.observeExt
import com.ramo.sweetsdk.state.DialogEvent
import com.ramo.sweetsdk.state.NavEvent

abstract class DataBindingActivity<DB : ViewDataBinding>(
    @LayoutRes private val layoutId: Int
) : AppCompatActivity() {

    private var _binding: DB? = null
    protected val binding: DB get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding?.lifecycleOwner = this
        _binding = DataBindingUtil.setContentView(this, layoutId)
        initCommonObserver()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    protected fun withVB(block: DB.() -> Unit) = with(binding, block)

    private fun initCommonObserver() {
        val viewModel = sweetViewModel() ?: return
        observeExt(viewModel.isLoading) { isLoading -> onChangeLoading(isLoading) }
        observeExt(viewModel.dialogEvent) { dialogEvent ->
            dismissAlertAndException()
            if (dialogEvent is DialogEvent.Alert)
                onAlert(dialogEvent.messageResId, dialogEvent.cancelable)
            else if (dialogEvent is DialogEvent.Error)
                onException(dialogEvent.throwable, dialogEvent.cancelable)

        }
        observeExt(viewModel.navigationEvent) { navEvent -> onNavigate(navEvent) }
    }


    protected abstract fun sweetViewModel(): SweetViewModel?

    protected abstract fun onChangeLoading(isLoading: Boolean)

    protected abstract fun onAlert(@StringRes stringId: Int, cancelable: Boolean)

    protected abstract fun onException(throwable: Throwable, cancelable: Boolean)

    protected abstract fun dismissAlertAndException()

    protected abstract fun onNavigate(navEvent: NavEvent)

}