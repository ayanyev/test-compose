package com.eazzyapps.repositories.domain

import com.eazzyapps.repositories.domain.models.CommitInfo
import com.eazzyapps.repositories.domain.models.GitHubRepo

interface Repository {

    suspend fun getPublicRepositories(owner: String): List<GitHubRepo>

    suspend fun getRepositoryById(id: Int): GitHubRepo

    suspend fun getRepositoryCommits(repo: GitHubRepo): List<CommitInfo>

}