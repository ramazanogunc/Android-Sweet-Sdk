package com.ramo.sweetsdk.demo.data

import kotlinx.coroutines.delay

class DefaultRepository {

    suspend fun getData(): String {
        delay(1800)
        return "Back end data"
    }

    suspend fun getDataWithException(): String {
        delay(1800)
        throw Exception("Excepiton state preview")
    }
}