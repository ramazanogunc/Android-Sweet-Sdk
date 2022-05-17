package com.ramo.core.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ramo.core.SweetViewModel
import com.ramo.core.ext.observeExt
import com.ramo.core.state.DialogEvent
import com.ramo.core.state.NavEvent

abstract class DataBindingFragment<DB : ViewDataBinding>(
    @LayoutRes private val layoutId: Int
) : Fragment() {
    private var _binding: DB? = null
    protected val binding: DB get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        _binding?.lifecycleOwner = viewLifecycleOwner
        initCommonObserver()
        return _binding!!.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
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

    protected open fun onNavigate(navEvent: NavEvent) {
        when (navEvent) {
            NavEvent.GoBack -> activity?.onBackPressed()
            is NavEvent.Navigate -> findNavController().navigate(navEvent.navDirections)
        }
    }

}