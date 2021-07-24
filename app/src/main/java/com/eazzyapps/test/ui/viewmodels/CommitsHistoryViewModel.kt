package com.eazzyapps.test.ui.viewmodels

import com.eazzyapps.test.OWNER
import com.eazzyapps.test.common.ActivityDelegate
import com.eazzyapps.test.common.BaseViewModel
import com.eazzyapps.test.domain.Repository
import com.eazzyapps.test.domain.models.CommitInfo
import com.eazzyapps.test.domain.models.GitHubRepo
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class CommitsHistoryViewModel(

    repo: GitHubRepo,
    repository: Repository,
    private val delegate: ActivityDelegate

) : BaseViewModel(delegate) {

    val monthFlow = MutableStateFlow(listOf<MonthViewModel>())

    private val updateFrequencyInMillis: Long = 1_500

    init {
        launch {
            delegate.showLoading(true)
            val commits = repository.getRepositoryCommits(OWNER, repo.name)
            startRotate(commits)
        }
    }

    private var currentChunk = 0

    private suspend fun startRotate(commits: List<CommitInfo>) {

        val commitsCountMap = commits
            .groupBy(
                keySelector = { it.date.parseToMonthYear() },
                valueTransform = { 1 }
            )
            .mapValues { (_, v) -> v.sum() }

        val maxMonthlyCount = commitsCountMap.values.maxOrNull() ?: 0

        val monthlyViewModels = commitsCountMap
            .map { (k, v) -> MonthViewModel(maxMonthlyCount, v, k) }
            .chunked(3)

        val chunksCount = monthlyViewModels.size

        delegate.showLoading(false)

        val tickerChannel = ticker(delayMillis = updateFrequencyInMillis, initialDelayMillis = 0)

        for (event in tickerChannel) {
            monthFlow.value = listOfNotNull(
                monthlyViewModels[currentChunk][0],
                monthlyViewModels.getOrNull(currentChunk)?.getOrNull(1),
                monthlyViewModels.getOrNull(currentChunk)?.getOrNull(2)
            )
            if (currentChunk == chunksCount - 1) currentChunk = 0
            else currentChunk++
        }
    }

    private fun String.parseToMonthYear(): String {
        val fromFormat = "yyyy-MM-dd'T'HH:mm:ss"
        val toFormat = "MMMyy"
        return SimpleDateFormat(fromFormat, Locale.getDefault()).parse(this)?.let { date ->
            SimpleDateFormat(toFormat, Locale.getDefault()).format(date)
        } ?: throw IllegalArgumentException("Commit date not parsed: $this")
    }

}