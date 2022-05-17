package com.ramo.core

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.ramo.core.lifecycle.SingleLiveEvent
import com.ramo.core.state.DialogEvent
import com.ramo.core.state.NavEvent
import kotlinx.coroutines.launch

abstract class SweetViewModel : ViewModel() {

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _dialogEvent = MutableLiveData<DialogEvent>()
    val dialogEvent: LiveData<DialogEvent> = _dialogEvent

    private val _navigationEvent = SingleLiveEvent<NavEvent>()
    val navigationEvent: LiveData<NavEvent> = _navigationEvent

    open fun handleSafeException(e: Exception) {
        showError(e)
    }

    protected fun safeScope(
        loadingVisible: Boolean = true,
        customHandleException: ((Exception) -> Unit)? = null,
        block: suspend () -> Unit
    ) {
        viewModelScope.launch {
            if (loadingVisible) showLoading()
            try {
                block()
            } catch (e: Exception) {
                if (customHandleException != null)
                    customHandleException.invoke(e)
                else
                    handleSafeException(e)
            }
            if (loadingVisible) hideLoading()
        }
    }

    fun showLoading() {
        _isLoading.value = true
    }

    fun hideLoading() {
        _isLoading.value = false
    }

    fun showAlert(@StringRes messageResId: Int, cancelable: Boolean = true) {
        _dialogEvent.value = DialogEvent.Alert(messageResId, cancelable)
    }

    fun showError(exception: Throwable, cancelable: Boolean = false) {
        _dialogEvent.value = DialogEvent.Error(exception, cancelable)
    }

    fun hideDialogs() {
        _dialogEvent.value = DialogEvent.None
    }

    fun navigate(navDirections: NavDirections) {
        _navigationEvent.value = NavEvent.Navigate(navDirections)
    }

    fun goBack() {
        _navigationEvent.value = NavEvent.GoBack
    }
}