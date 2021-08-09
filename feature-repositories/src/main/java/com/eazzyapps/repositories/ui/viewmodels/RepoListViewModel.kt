package com.eazzyapps.repositories.ui.viewmodels

import androidx.paging.map
import com.eazzyapps.repositories.ACCOUNT_OWNER
import com.eazzyapps.repositories.domain.Repository
import com.eazzyapps.repositories.ui.navigation.RepositoryScreen.RepoDetailsScreen
import com.eazzyapps.test.common.ActivityDelegate
import com.eazzyapps.test.common.BaseViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class RepoListViewModel(

    repo: Repository,
    private val delegate: ActivityDelegate

) : BaseViewModel(delegate) {

    val reposFlow = repo.getPagedPublicRepositories(ACCOUNT_OWNER).map { data ->
        data.map { repo ->
            RepoItemViewModel(
                repo = repo,
                onClick = { showDetails(repo.id) }
            )
        }
    }

    private fun showDetails(repoId: Int) {
        launch { delegate.navigate(RepoDetailsScreen.withRouteArgs(repoId)) }
    }

}