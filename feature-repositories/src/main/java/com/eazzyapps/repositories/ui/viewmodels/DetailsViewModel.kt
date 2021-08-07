package com.eazzyapps.repositories.ui.viewmodels

import com.eazzyapps.repositories.domain.Repository
import com.eazzyapps.repositories.domain.models.GitHubRepo
import com.eazzyapps.test.common.ActivityDelegate
import com.eazzyapps.test.common.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailsViewModel(

    repoId: Int,
    repository: Repository,
    delegate: ActivityDelegate

) : BaseViewModel(delegate) {

    private val infoFlow = MutableStateFlow<Info?>(null)

    private val commitsVm = CommitsHistoryViewModel(repository, delegate)

    val info = infoFlow.asStateFlow()

    val commits = commitsVm.monthFlow.asStateFlow()

    init {

        launch {
            repository.getRepositoryById(repoId).also { repo ->
                infoFlow.value = Info(repo)
                commitsVm.setRepository(repo)
            }
        }

    }

    class Info(repo: GitHubRepo) {

        val id: String = "${repo.id}"

        val name: String = repo.name

        val owner: String = repo.owner

        val desc: String = repo.description ?: "no description provided"

        val license: String = repo.license ?: "no license selected"

        val date: String = repo.createdAt ?: "no date"

    }

}