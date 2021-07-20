package com.eazzyapps.test.ui.viewmodels

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.eazzyapps.test.domain.Repository
import com.eazzyapps.test.domain.models.GitHubRepo
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject

class MainViewModel(repo: Repository) : ViewModel() {

    private val disposables = CompositeDisposable()

    private val repoClickSubject = PublishSubject.create<GitHubRepo>()

    val itemClicks: Observable<GitHubRepo> = repoClickSubject

    val publicRepos = ObservableField<List<RepoItemViewModel>>()

    val isLoading = ObservableBoolean(false)

    val errorMsg = ObservableField("")

    val errorMsgVisibility = ObservableField(false)

    var selectedRepo: GitHubRepo? = null

    init {

        disposables.add(
            repo.getPublicRepositories(OWNER)
                .doOnSubscribe { isLoading.set(true) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { isLoading.set(false) }
                .subscribeBy(
                    onNext = { repos ->
                        hideError()
                        publicRepos.set(
                            repos.map { repo ->
                                RepoItemViewModel(
                                    repo = repo,
                                    onClick = { r ->
                                        selectedRepo = r
                                        repoClickSubject.onNext(r)
                                    }
                                )
                            }
                        )
                    },
                    onError = { e ->
                        isLoading.set(false)
                        showError(e.message ?: "Smth happened!!!")
                    }
                )
        )
    }

    private fun showError(msg: String) {
        errorMsg.set(msg)
        errorMsgVisibility.set(true)
    }

    private fun hideError() {
        errorMsgVisibility.set(false)
    }

    override fun onCleared() {
        disposables.dispose()
    }

    companion object {

        const val OWNER = "JakeWharton"

    }

}