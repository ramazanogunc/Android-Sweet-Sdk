package com.ramo.sweetsdk.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ramo.sweetsdk.SweetViewModel
import com.ramo.sweetsdk.ext.observeExt
import com.ramo.sweetsdk.state.DialogEvent
import com.ramo.sweetsdk.state.NavEvent

abstract class NormalFragment(
    @LayoutRes private val layoutId: Int
) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutId, container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

    protected open fun onNavigate(navEvent: NavEvent) {
        when (navEvent) {
            NavEvent.GoBack -> activity?.onBackPressed()
            is NavEvent.Navigate -> findNavController().navigate(navEvent.navDirections)
        }
    }

}