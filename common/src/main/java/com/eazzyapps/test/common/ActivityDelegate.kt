package com.eazzyapps.test.common

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class ActivityDelegate {

    // loading

    private val _loadingFlow = MutableStateFlow(false)

    val loadingFlow = _loadingFlow.asStateFlow()

    suspend fun showLoading(isLoading: Boolean) {
        throttleLoadingInterval(isLoading)
        _loadingFlow.value = isLoading
    }

    private val minLoadingInterval = 1000L

    private var loadingStartTime = System.currentTimeMillis()

    private suspend fun throttleLoadingInterval(isLoading: Boolean) {
        if (isLoading) loadingStartTime = System.currentTimeMillis()
        else {
            val actualLoadingInterval = System.currentTimeMillis() - loadingStartTime
            delay(minLoadingInterval - actualLoadingInterval)
        }
    }

    // messaging

    private val _msgFlow = MutableSharedFlow<Message>()

    val msgFlow = _msgFlow.asSharedFlow()

    suspend fun showMessage(msg: Message) {
        _msgFlow.emit(msg)
    }

    // navigating

    private val _navFlow = MutableSharedFlow<Screen>()

    val navFlow = _navFlow.asSharedFlow()

    suspend fun navigate(event: Screen) {
        _navFlow.emit(event)
    }

}