package com.ramo.core.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.ramo.core.SweetViewModel
import com.ramo.core.ext.findGenericWithType
import com.ramo.core.ext.observeExt
import com.ramo.core.state.DialogEvent
import com.ramo.core.state.NavEvent
import java.lang.reflect.Method

abstract class ViewBindingFragment<VB : ViewBinding> : Fragment() {

    private var _binding: VB? = null
    protected val binding: VB get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val foundInflater = findInflateMethod()
        @Suppress("UNCHECKED_CAST")
        _binding = foundInflater.invoke(null, inflater, container, false) as VB
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCommonObserver()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    protected fun withVB(block: VB.() -> Unit) = with(binding, block)

    private fun findInflateMethod(): Method {
        val clazz = javaClass.findGenericWithType<VB>(0)
        return clazz.getMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.java
        )
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