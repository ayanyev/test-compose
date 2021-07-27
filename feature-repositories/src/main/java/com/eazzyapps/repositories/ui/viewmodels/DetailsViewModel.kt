package com.eazzyapps.repositories.ui.viewmodels

import com.eazzyapps.repositories.domain.Repository
import com.eazzyapps.repositories.domain.models.GitHubRepo
import com.eazzyapps.test.common.ActivityDelegate
import com.eazzyapps.test.common.BaseViewModel

class DetailsViewModel(

    repo: GitHubRepo,
    repository: Repository,
    delegate: ActivityDelegate

) : BaseViewModel(delegate) {

    val info = Info(repo)

    private val commitsVm = CommitsHistoryViewModel(repo, repository, delegate)

    val commits = commitsVm.monthFlow

    class Info(repo: GitHubRepo) {

        val id: String = "${repo.id}"

        val name: String = repo.name

        val owner: String = repo.owner

        val desc: String = repo.description ?: "no description provided"

        val license: String = repo.license ?: "no license selected"

        val date: String = repo.createdAt ?: "no date"

    }

}