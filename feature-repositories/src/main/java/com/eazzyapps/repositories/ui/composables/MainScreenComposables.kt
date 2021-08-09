package com.eazzyapps.repositories.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.eazzyapps.repositories.R
import com.eazzyapps.repositories.domain.fakeRepository
import com.eazzyapps.repositories.ui.theme.typography
import com.eazzyapps.repositories.ui.viewmodels.RepoListViewModel
import com.eazzyapps.repositories.ui.viewmodels.RepoItemViewModel
import com.eazzyapps.test.common.Message
import com.eazzyapps.test.common.ProgressIndicator
import org.koin.androidx.compose.getViewModel
import java.lang.Exception

@Composable
fun MainScreen() {

    val vm = getViewModel<RepoListViewModel>()

    val lazyRepoItems = vm.reposFlow.collectAsLazyPagingItems()

    Box(Modifier.fillMaxSize()) {
        ReposList(lazyRepoItems = lazyRepoItems)
    }

    lazyRepoItems.loadState.apply {
        if (refresh is LoadState.Error || append is LoadState.Error) {
            vm.showMessage(Message.SnackBar(R.string.pagination_error))
        }
    }

}

@Composable
fun ReposList(lazyRepoItems: LazyPagingItems<RepoItemViewModel>) {

    val listState = rememberLazyListState()

    LazyColumn(
        Modifier
            .fillMaxSize()
            .testTag("repoList"), state = listState
    ) {
        itemsIndexed(lazyRepoItems) { i, repo ->
            if (repo == null) return@itemsIndexed
            RepoItem(repo = repo)
            if (i < lazyRepoItems.itemCount) {
                Divider()
            }
        }
    }

    val ls = lazyRepoItems.loadState

    ProgressIndicator(
        modifier = when {
            ls.refresh == LoadState.Loading -> Modifier.fillMaxSize()
            ls.append == LoadState.Loading -> Modifier.height(64.dp).fillMaxWidth()
            else -> Modifier
        },
        isLoading = when {
            ls.refresh == LoadState.Loading -> true
            ls.append == LoadState.Loading -> true
            else -> false
        }
    )
}

@Composable
fun RepoItem(repo: RepoItemViewModel) {
    Column(
        Modifier
            .testTag("repoItem")
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(16.dp)
            .clickable { repo.doOnClick() }
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.watch),
                stringResource(R.string.cont_desc_icon_watchers)
            )
            Text(modifier = Modifier.padding(horizontal = 4.dp), text = repo.watchersCount)
            Icon(
                painter = painterResource(id = R.drawable.star),
                stringResource(R.string.cont_desc_icon_stars)
            )
            Text(modifier = Modifier.padding(horizontal = 4.dp), text = repo.starsCount)
            Icon(
                painter = painterResource(id = R.drawable.fork),
                stringResource(R.string.cont_desc_icon_forks)
            )
            Text(modifier = Modifier.padding(horizontal = 4.dp), text = repo.forksCount)
        }
        Text(text = repo.name, style = typography.h6)
        Text(text = repo.desc)
    }
}

/*@Preview
@Composable
fun ReposListPreview() {
    ReposList(repos = listOf(
        RepoItemViewModel(fakeRepository, onClick = {}),
        RepoItemViewModel(fakeRepository, onClick = {}),
        RepoItemViewModel(fakeRepository, onClick = {})
    )
    )
}*/

@Preview
@Composable
fun RepoItemPreview() {
    RepoItem(repo = RepoItemViewModel(fakeRepository, onClick = {}))
}