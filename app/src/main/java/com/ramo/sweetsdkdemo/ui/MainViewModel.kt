package com.ramo.sweetsdkdemo.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ramo.sweetsdk.SweetViewModel
import com.ramo.sweetsdkdemo.data.DefaultRepository

class MainViewModel(
    private val defaultRepository: DefaultRepository = DefaultRepository()
) : SweetViewModel() {

    private val _data = MutableLiveData<String>()
    val data: LiveData<String> = _data

    fun getData() = safeScope {
        _data.value = defaultRepository.getData()
    }

    fun refreshData() = safeScope {
        _data.value = defaultRepository.getDataWithException()
    }
}