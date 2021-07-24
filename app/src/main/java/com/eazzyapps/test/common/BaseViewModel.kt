package com.eazzyapps.test.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eazzyapps.test.R
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel(

    private val delegate: ActivityDelegate

) : ViewModel(), CoroutineScope {

    private val handler = CoroutineExceptionHandler { _, throwable ->
        launch { showError(throwable) }
    }

    override val coroutineContext: CoroutineContext
        get() = viewModelScope.plus(SupervisorJob()).plus(handler).coroutineContext

    suspend fun showError(e: Throwable) {
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
        cancel()
        launch { delegate.showLoading(false) }
    }
}