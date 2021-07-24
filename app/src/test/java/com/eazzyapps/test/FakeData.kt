package com.eazzyapps.test

import com.eazzyapps.test.domain.models.CommitInfo
import com.eazzyapps.test.domain.models.GitHubRepo
import com.eazzyapps.test.ui.viewmodels.RepoItemViewModel

val fakeRepoId = 1111

val fakeRepository = GitHubRepo(
    id = fakeRepoId,
    name = "Very popular repository",
    description = "So interesting description",
    owner = ACCOUNT_OWNER,
    avatarUrl = "",
    createdAt = "2018-10-18T00:00:00Z",
    license = "Free to use",
    watchers = 10,
    stars = 23,
    forks = 5
)

val fakeRepositoriesList = listOf(
    fakeRepository, fakeRepository, fakeRepository,
    fakeRepository, fakeRepository, fakeRepository
)

val fakeRepoViewModelsList = fakeRepositoriesList
    .map { RepoItemViewModel(it) {} }

val fakeCommitInfo = CommitInfo(
    sha = "4ty56678678776",
    date = "2018-10-18T00:00:00Z",
    author = ACCOUNT_OWNER,
    message = "Initial commit"
)

val fakeCommits = listOf(
    fakeCommitInfo, fakeCommitInfo, fakeCommitInfo
)