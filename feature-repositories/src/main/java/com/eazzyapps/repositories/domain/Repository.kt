package com.eazzyapps.repositories.domain

import androidx.paging.PagingData
import com.eazzyapps.repositories.domain.models.CommitInfo
import com.eazzyapps.repositories.domain.models.GitHubRepo
import kotlinx.coroutines.flow.Flow

interface Repository {

    suspend fun getRepositoryById(id: Int): GitHubRepo

    suspend fun getPublicRepositories(owner: String): List<GitHubRepo>

    fun getPagedPublicRepositories(owner: String): Flow<PagingData<GitHubRepo>>

    suspend fun getRepositoryCommits(repo: GitHubRepo): List<CommitInfo>

}