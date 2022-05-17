package com.ramo.core.ext

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

fun Fragment.safeContext(func: (Context) -> Unit) {
    context?.let(func)
}

fun <T> Fragment.observeExt(liveData: LiveData<T>, observer: (T) -> Unit) {
    liveData.observe(viewLifecycleOwner) { it?.let { t -> observer(t) } }
}