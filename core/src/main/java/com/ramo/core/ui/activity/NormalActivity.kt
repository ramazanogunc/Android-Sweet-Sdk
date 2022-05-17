package com.ramo.core.ui.activity

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.ramo.core.SweetViewModel
import com.ramo.core.ext.observeExt
import com.ramo.core.state.DialogEvent
import com.ramo.core.state.NavEvent

abstract class NormalActivity(
    @LayoutRes private val layoutId: Int
) : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
        initCommonObserver()
    }

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