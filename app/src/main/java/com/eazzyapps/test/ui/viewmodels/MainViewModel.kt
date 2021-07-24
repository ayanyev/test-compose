package com.eazzyapps.test.ui.viewmodels

import com.eazzyapps.test.OWNER
import com.eazzyapps.test.common.ActivityDelegate
import com.eazzyapps.test.common.BaseViewModel
import com.eazzyapps.test.domain.Repository
import com.eazzyapps.test.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(

    repo: Repository,
    delegate: ActivityDelegate

) : BaseViewModel(delegate) {

    private val _reposFlow = MutableStateFlow<List<RepoItemViewModel>>(listOf())

    val reposFlow = _reposFlow.asStateFlow()

    init {

        launch {
            delegate.showLoading(true)
            repo.getPublicRepositories(OWNER).also { repos ->
                val reposVM = repos.map { repo ->
                    RepoItemViewModel(
                        repo = repo,
                        onClick = {
                            delegate.navigate(Screen.Details(repo))
                        }
                    )
                }
                _reposFlow.value = reposVM
            }
            delegate.showLoading(false)
        }
    }

}