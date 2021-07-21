package com.eazzyapps.test.ui.viewmodels

import androidx.databinding.ObservableField
import com.eazzyapps.test.common.BaseViewModel
import com.eazzyapps.test.domain.Repository
import com.eazzyapps.test.domain.models.GitHubRepo
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class MainViewModel(repo: Repository) : BaseViewModel() {

    private val _clickFlow = MutableSharedFlow<Boolean>()

    val clickFlow = _clickFlow.asSharedFlow()

    val publicRepos = ObservableField<List<RepoItemViewModel>>()

    var selectedRepo: GitHubRepo? = null
        private set(value) {
            field = value
            launch {
                _clickFlow.emit(true)
            }
        }

    init {

        launch {
            isLoading.set(true)
            repo.getPublicRepositories(OWNER).also { repos ->
                val reposVM = repos.map { repo ->
                    RepoItemViewModel(
                        repo = repo,
                        onClick = { selectedRepo = it }
                    )
                }
                publicRepos.set(reposVM)
            }
            isLoading.set(false)
        }
    }

    companion object {

        const val OWNER = "JakeWharton"

    }

}