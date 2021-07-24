package com.eazzyapps.test.domain

import com.eazzyapps.test.domain.models.CommitInfo
import com.eazzyapps.test.domain.models.GitHubRepo

interface Repository {

    suspend fun getPublicRepositories(owner: String): List<GitHubRepo>

    suspend fun getRepositoryCommits(owner: String, repoName: String): List<CommitInfo>

}