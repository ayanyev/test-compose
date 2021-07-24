package com.eazzyapps.test.ui.viewmodels

import com.eazzyapps.test.ACCOUNT_OWNER
import com.eazzyapps.test.common.ActivityDelegate
import com.eazzyapps.test.common.BaseViewModel
import com.eazzyapps.test.domain.Repository
import com.eazzyapps.test.domain.models.GitHubRepo
import com.eazzyapps.test.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(

    repo: Repository,
    private val delegate: ActivityDelegate

) : BaseViewModel(delegate) {

    private val _reposFlow = MutableStateFlow<List<RepoItemViewModel>>(listOf())

    val reposFlow = _reposFlow.asStateFlow()

    init {

        launch {
            delegate.showLoading(true)
            repo.getPublicRepositories(ACCOUNT_OWNER).also { repos ->
                val reposVM = repos.map { repo ->
                    RepoItemViewModel(
                        repo = repo,
                        onClick = { showDetails(it) }
                    )
                }
                _reposFlow.value = reposVM
            }
            delegate.showLoading(false)
        }
    }

    private fun showDetails(repo: GitHubRepo) {
        launch { delegate.navigate(Screen.Details(repo)) }
    }

}