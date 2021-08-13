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

    override val coroutineContext: CoroutineContext by lazy {
        viewModelScope.plus(handler).coroutineContext
    }

    open suspend fun handleError(e: Throwable) {
        delegate.showLoading(false)
        delegate.showMessage(
            Message.SnackBar(
                R.string.error_template,
                e.message ?: "Unknown error"
            )
        )
    }

    override fun onCleared() {
        super.onCleared()
        launch { delegate.showLoading(false) }
        coroutineContext.cancelChildren()
    }
}