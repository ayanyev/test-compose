package com.eazzyapps.test.ui.customviews.commitshistory

import android.util.Log
import androidx.databinding.ObservableField
import com.eazzyapps.test.common.BaseViewModel
import com.eazzyapps.test.domain.models.CommitInfo
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class CommitsHistoryViewModel(
    commits: List<CommitInfo>,
    private val updateFrequencyInMillis: Long = 1_500
) : BaseViewModel() {

    val monthOne = ObservableField<MonthViewModel>()

    val monthTwo = ObservableField<MonthViewModel>()

    val monthThee = ObservableField<MonthViewModel>()

    private val commitsCountMap = commits
        .groupBy(
            keySelector = { it.date.parseToMonthYear() },
            valueTransform = { 1 }
        )
        .mapValues { (_, v) -> v.sum() }

    private val maxMonthlyCount = commitsCountMap.values.maxOrNull() ?: 0

    private val monthlyViewModels = commitsCountMap
        .map { (k, v) -> MonthViewModel(maxMonthlyCount, v, k) }
        .chunked(3)

    init { startRotate() }

    private var disposable: Disposable? = null

    private var currentChunk = 0

    fun startRotate() {
        val chunksCount = monthlyViewModels.size
        Log.d(javaClass.simpleName, "months=$chunksCount")

        val tickerChannel = ticker(delayMillis = updateFrequencyInMillis, initialDelayMillis = 0)

        launch {
            for (event in tickerChannel) {
                monthOne.set(monthlyViewModels[currentChunk][0])
                monthTwo.set(monthlyViewModels.getOrNull(currentChunk)?.getOrNull(1))
                monthThee.set(monthlyViewModels.getOrNull(currentChunk)?.getOrNull(2))
                if (currentChunk == chunksCount - 1) currentChunk = 0
                else currentChunk++
            }
        }
    }

    fun stopRotate() {
        disposable?.dispose()
    }

    private fun String.parseToMonthYear(): String {
        val fromFormat = "yyyy-MM-dd'T'HH:mm:ss"
        val toFormat = "MMMyy"
        return SimpleDateFormat(fromFormat, Locale.getDefault()).parse(this)?.let { date ->
            SimpleDateFormat(toFormat, Locale.getDefault()).format(date)
        } ?: throw IllegalArgumentException("Commit date not parsed: $this")
    }

}