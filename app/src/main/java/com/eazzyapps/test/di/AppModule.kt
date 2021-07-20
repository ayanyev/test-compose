package com.eazzyapps.test.di

import com.eazzyapps.test.data.RemoteClient
import com.eazzyapps.test.data.RepoClient
import com.eazzyapps.test.data.RepositoryImpl
import com.eazzyapps.test.domain.Repository
import com.eazzyapps.test.ui.viewmodels.DetailsViewModel
import com.eazzyapps.test.ui.viewmodels.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single { RemoteClient.create(RepoClient::class.java) }

    factory <Repository> { RepositoryImpl(get()) }

    viewModel { MainViewModel(get()) }

    viewModel { repo -> DetailsViewModel(repo = repo.get(), get()) }

}