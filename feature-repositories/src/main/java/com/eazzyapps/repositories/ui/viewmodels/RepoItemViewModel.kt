package com.eazzyapps.repositories.ui.viewmodels

import com.eazzyapps.repositories.domain.models.GitHubRepo

class RepoItemViewModel(
    private val repo: GitHubRepo,
    private val onClick: (GitHubRepo) -> Unit
) {

    val name: String = repo.name

    val desc: String = repo.description ?: "no description provided"

    val forksCount: String = "${repo.forks}"

    val watchersCount: String = "${repo.watchers}"

    val starsCount: String = "${repo.stars}"

    fun doOnClick() { onClick(repo) }

}