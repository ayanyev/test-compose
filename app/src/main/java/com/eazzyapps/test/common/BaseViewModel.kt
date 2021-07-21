package com.eazzyapps.test.common

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel : ViewModel(), CoroutineScope {

    val isLoading = ObservableBoolean(false)

    private val _errorFlow = MutableSharedFlow<String>()

    val errorFlow = _errorFlow.asSharedFlow()

    private val handler = CoroutineExceptionHandler { _, throwable ->
        launch {
            showError(throwable)
        }
    }

    override val coroutineContext: CoroutineContext
        get() = viewModelScope.plus(SupervisorJob()).plus(handler).coroutineContext

    protected suspend fun showError(e: Throwable) {
        isLoading.set(false)
        _errorFlow.emit(e.message ?: "Unknown error")
    }
}