package com.eazzyapps.test.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel(

    private val delegate: ActivityDelegate

) : ViewModel(), CoroutineScope {

    private val handler = CoroutineExceptionHandler { _, throwable ->
        launch { handleError(throwable) }
    }

    override val coroutineContext: CoroutineContext
        get() = viewModelScope.plus(SupervisorJob()).plus(handler).coroutineContext

    open suspend fun handleError(e: Throwable) {
        delegate.showLoading(false)
        delegate.showMessage(
            Message.SnackBar(
                R.string.error_template,
                e.message ?: "Unknown error"
            )
        )
    }

    fun showMessage(msg: Message) {
        launch {
            delegate.showMessage(msg)
        }
    }

    override fun onCleared() {
        super.onCleared()
        cancel()
        launch { delegate.showLoading(false) }
    }
}