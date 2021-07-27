package com.eazzyapps.repositories.di

import com.eazzyapps.repositories.data.remote.retrofit.RemoteClient
import com.eazzyapps.repositories.data.remote.retrofit.RepoClient
import com.eazzyapps.repositories.data.RepositoryImpl
import com.eazzyapps.repositories.domain.Repository
import com.eazzyapps.repositories.ui.viewmodels.DetailsViewModel
import com.eazzyapps.repositories.ui.viewmodels.RepoListViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val repositoryModule = module {

    single { RemoteClient.create(RepoClient::class.java) }

    factory { Dispatchers.IO }

    factory <Repository> { RepositoryImpl(get(), get()) }

    viewModel { RepoListViewModel(get(), get()) }

    viewModel { repo -> DetailsViewModel(repo = repo.get(), get(), get()) }

}