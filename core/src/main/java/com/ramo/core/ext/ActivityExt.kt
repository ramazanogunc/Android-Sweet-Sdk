package com.ramo.core.ext

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

fun <T> AppCompatActivity.observeExt(liveData: LiveData<T>, observer: (T) -> Unit) {
    liveData.observe(this) { it?.let { t -> observer(t) } }
}