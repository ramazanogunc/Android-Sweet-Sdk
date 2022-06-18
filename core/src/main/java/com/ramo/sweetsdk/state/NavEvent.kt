package com.ramo.sweetsdk.state

import androidx.navigation.NavDirections

sealed class NavEvent {
    class Navigate(val navDirections: NavDirections) : NavEvent()
    object GoBack : NavEvent()
}