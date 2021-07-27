package com.eazzyapps.test.common

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.*
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class TestCoroutineRule : TestWatcher(), TestCoroutineScope by TestCoroutineScope() {

    private val testDispatcher = TestCoroutineDispatcher()

    override fun starting(description: Description) {
        super.starting(description)
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        super.finished(description)
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }


    fun runBlockingTest(block: suspend TestCoroutineScope.() -> Unit) =
        testDispatcher.runBlockingTest(block)

}