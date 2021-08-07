package com.eazzyapps.repositories.di

import com.eazzyapps.repositories.Database
import com.eazzyapps.repositories.data.RepositoryImpl
import com.eazzyapps.repositories.data.remote.retrofit.RemoteClient
import com.eazzyapps.repositories.data.remote.retrofit.RepoClient
import com.eazzyapps.repositories.domain.Repository
import com.eazzyapps.repositories.ui.viewmodels.DetailsViewModel
import com.eazzyapps.repositories.ui.viewmodels.RepoListViewModel
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val repositoryModule = module {

    single { RemoteClient.create(RepoClient::class.java) }

    single<SqlDriver> { AndroidSqliteDriver(Database.Schema, androidApplication(), "test.db") }

    single {
        Database(get()).apply {
            // clear tables on app start
            githubRepoTableQueries.deleteAll()
            commitInfoTableQueries.deleteAll()
        }
    }

    factory { Dispatchers.IO }

    factory <Repository> { RepositoryImpl(get(), get(), get()) }

    viewModel { RepoListViewModel(get(), get()) }

    viewModel { repoId -> DetailsViewModel(repoId.get(), get(), get()) }

}