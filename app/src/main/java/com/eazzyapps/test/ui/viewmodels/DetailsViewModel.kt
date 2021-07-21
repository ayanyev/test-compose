package com.eazzyapps.test.ui.viewmodels

import androidx.databinding.ObservableField
import com.eazzyapps.test.common.BaseViewModel
import com.eazzyapps.test.domain.Repository
import com.eazzyapps.test.domain.models.GitHubRepo
import com.eazzyapps.test.ui.customviews.commitshistory.CommitsHistoryViewModel
import kotlinx.coroutines.launch

class DetailsViewModel(
    repo: GitHubRepo,
    repository: Repository
) : BaseViewModel() {

    val id: String = "${repo.id}"

    val name: String = repo.name

    val owner: String = repo.owner

    val desc: String = repo.description ?: "no description provided"

    val license: String = repo.license ?: "no license selected"

    val date: String = repo.createdAt ?: "no date"

    private var commitsVm: CommitsHistoryViewModel? = null

    val commitsViewModel = ObservableField<CommitsHistoryViewModel>(commitsVm)

    init {

        launch {
            isLoading.set(true)
            repository.getRepositoryCommits(OWNER, repo.name).also { commits ->
                commitsVm = CommitsHistoryViewModel(commits)
                commitsViewModel.set(commitsVm)
            }
            isLoading.set(false)
        }

    }

    fun onResume() {
        commitsVm?.startRotate()
    }

    fun onPause() {
        commitsVm?.stopRotate()
    }

    companion object {

        const val OWNER = "JakeWharton"

    }

}