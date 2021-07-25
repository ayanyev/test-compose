package com.eazzyapps.test.ui.viewmodels

import com.eazzyapps.test.ACCOUNT_OWNER
import com.eazzyapps.test.TestCoroutineRule
import com.eazzyapps.test.common.ActivityDelegate
import com.eazzyapps.test.domain.Repository
import com.eazzyapps.test.data.fakeRepoViewModelsList
import com.eazzyapps.test.data.fakeRepositoriesList
import io.mockk.coEvery
import io.mockk.coVerifyOrder
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class MainViewModelTest {

    @get:Rule
    val coroutineRule = TestCoroutineRule()

    private val repository = mockk<Repository>()

    private val delegate = spyk<ActivityDelegate>()

    @Test
    fun `successfully fetch repositories test`() = coroutineRule.runBlockingTest {

        coEvery { repository.getPublicRepositories(ACCOUNT_OWNER) } returns fakeRepositoriesList

        val viewModel = MainViewModel(repository, delegate)

        coVerifyOrder {
            delegate.showLoading(true)
            repository.getPublicRepositories(ACCOUNT_OWNER)
            delegate.showLoading(false)
        }

        val output = viewModel.reposFlow.take(1).toList().first()

        assertEquals(fakeRepoViewModelsList.size, output.size)

    }

    @Test
    fun `fetch repositories with exception test`() = coroutineRule.runBlockingTest {

        val e = Exception("Wrong credentials")

        coEvery { repository.getPublicRepositories(ACCOUNT_OWNER) } throws e

        val viewModel = MainViewModel(repository, delegate)

        coVerifyOrder {
            delegate.showLoading(true)
            repository.getPublicRepositories(ACCOUNT_OWNER)
            delegate.showLoading(false)
        }

        val output = viewModel.reposFlow.take(1).toList().first()

        assert(output.isEmpty())

    }

}