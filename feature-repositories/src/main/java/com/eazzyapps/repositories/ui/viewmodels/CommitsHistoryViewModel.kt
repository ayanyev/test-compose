package com.eazzyapps.repositories.ui.viewmodels

import com.eazzyapps.repositories.domain.Repository
import com.eazzyapps.repositories.domain.models.CommitInfo
import com.eazzyapps.repositories.domain.models.GitHubRepo
import com.eazzyapps.test.common.ActivityDelegate
import com.eazzyapps.test.common.BaseViewModel
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class CommitsHistoryViewModel(

    private val repository: Repository,
    private val delegate: ActivityDelegate

) : BaseViewModel(delegate) {

    val monthFlow = MutableStateFlow(listOf<MonthViewModel>())

    private val updateFrequencyInMillis: Long = 1_500

    fun setRepository(repo: GitHubRepo) {
        launch {
            delegate.showLoading(true)
            val commits = repository.getRepositoryCommits(repo)
            startRotate(commits)
        }
    }

    private var currentChunk = 0

    private suspend fun startRotate(commits: List<CommitInfo>) {

        val commitsCountMap = commits
            .groupBy(
                keySelector = { it.date.parseToMonthYear(Locale.getDefault()) },
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
}

fun String.parseToMonthYear(local: Locale): String {
    val fromFormat = "yyyy-MM-dd'T'HH:mm:ss"
    val toFormat = "MMMyy"
    return SimpleDateFormat(fromFormat, local).parse(this)?.let { date ->
        SimpleDateFormat(toFormat, local).format(date)
    } ?: throw ParseException("Commit date not parsed: $this", 0)
}