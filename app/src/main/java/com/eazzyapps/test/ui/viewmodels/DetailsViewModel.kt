package com.eazzyapps.test.ui.viewmodels

import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.eazzyapps.test.domain.Repository
import com.eazzyapps.test.domain.models.GitHubRepo
import com.eazzyapps.test.ui.customviews.commitshistory.CommitsHistoryViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers

class DetailsViewModel(
    repo: GitHubRepo,
    repository: Repository
) : ViewModel() {

    private val disposables = CompositeDisposable()

    val id: String = "${repo.id}"

    val name: String = repo.name

    val owner: String = repo.owner

    val desc: String = repo.description ?: "no description provided"

    val license: String = repo.license ?: "no license selected"

    val date: String = repo.createdAt ?: "no date"

    val isLoading = ObservableBoolean(false)

    private var commitsVm: CommitsHistoryViewModel? = null

    val commitsViewModel = ObservableField<CommitsHistoryViewModel>(commitsVm)

    init {

        disposables.add(
            repository.getRepositoryCommits(OWNER, repo.name)
                .doOnSubscribe { isLoading.set(true) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { isLoading.set(false) }
                .subscribeBy(
                    onNext = { commits ->
                        commitsVm = CommitsHistoryViewModel(commits)
                        commitsViewModel.set(commitsVm)
                    },
                    onError = { e -> Log.e(javaClass.simpleName, e.message ?: "smth happened") }
                )
        )

    }

    fun onResume() {
        commitsVm?.startRotate()
    }

    fun onPause() {
        commitsVm?.stopRotate()
    }

    override fun onCleared() {
        disposables.dispose()
    }

    companion object {

        const val OWNER = "JakeWharton"

    }

}