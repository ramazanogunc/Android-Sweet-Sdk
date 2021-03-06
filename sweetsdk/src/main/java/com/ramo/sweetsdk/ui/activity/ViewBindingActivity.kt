package com.ramo.sweetsdk.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.ramo.sweetsdk.SweetViewModel
import com.ramo.sweetsdk.ext.findGenericWithType
import com.ramo.sweetsdk.ext.observeExt
import com.ramo.sweetsdk.state.DialogEvent
import com.ramo.sweetsdk.state.NavEvent
import java.lang.reflect.Method

abstract class ViewBindingActivity<VB : ViewBinding> : AppCompatActivity() {

    private var _binding: VB? = null
    protected val binding: VB get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val foundInflater = findInflateMethod()
        @Suppress("UNCHECKED_CAST")
        _binding = foundInflater.invoke(null, layoutInflater) as VB
        setContentView(binding.root)
        initCommonObserver()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    protected fun withVB(block: VB.() -> Unit) = with(binding, block)

    private fun findInflateMethod(): Method {
        val clazz = javaClass.findGenericWithType<VB>(0)
        return clazz.getMethod(
            "inflate",
            LayoutInflater::class.java,
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

    protected abstract fun onNavigate(navEvent: NavEvent)
}