package com.eazzyapps.test.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eazzyapps.test.R
import com.eazzyapps.test.domain.models.GitHubRepo
import com.eazzyapps.test.ui.theme.typography
import com.eazzyapps.test.ui.viewmodels.MainViewModel
import com.eazzyapps.test.ui.viewmodels.RepoItemViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun MainScreen() {

    val vm = getViewModel<MainViewModel>()

    val repos by vm.reposFlow.collectAsState()

    Box(Modifier.fillMaxSize()) {
        ReposList(repos = repos)
    }

}

@Composable
fun ReposList(repos: List<RepoItemViewModel>) {

    val listState = rememberLazyListState()

    LazyColumn(Modifier.fillMaxSize(), state = listState) {
        itemsIndexed(repos) { i, repo ->
            RepoItem(repo = repo)
            if (i < repos.lastIndex) {
                Divider()
            }
        }
    }
}

@Preview
@Composable
fun ReposListPreview() {
    ReposList(repos = listOf(
        RepoItemViewModel(sampleRepo, onClick = {}),
        RepoItemViewModel(sampleRepo, onClick = {}),
        RepoItemViewModel(sampleRepo, onClick = {}))
    )
}

@Composable
fun RepoItem(repo: RepoItemViewModel) {
    Column(
        Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(16.dp)
            .clickable { repo.doOnClick() }
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Icon(painter = painterResource(id = R.drawable.watch), null)
            Text(modifier = Modifier.padding(horizontal = 4.dp), text = repo.watchersCount)
            Icon(painter = painterResource(id = R.drawable.star), null)
            Text(modifier = Modifier.padding(horizontal = 4.dp), text = repo.starsCount)
            Icon(painter = painterResource(id = R.drawable.fork), null)
            Text(modifier = Modifier.padding(horizontal = 4.dp), text = repo.forksCount)
        }
        Text(text = repo.name, style = typography.h6)
        Text(text = repo.desc)
    }
}

@Preview
@Composable
fun RepoItemPreview() {
    RepoItem(repo = RepoItemViewModel(sampleRepo, onClick = {}))
}

internal val sampleRepo = GitHubRepo(
    id = 11111,
    name = "Some repository",
    description = "Some looooong description",
    owner = "Some passionate developer",
    avatarUrl = "",
    createdAt = "10.12.2008",
    license = "MIT License",
    watchers = 10,
    stars = 23,
    forks = 5
)