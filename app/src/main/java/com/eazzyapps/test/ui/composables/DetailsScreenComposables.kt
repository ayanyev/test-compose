package com.eazzyapps.test.ui.composables

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eazzyapps.test.data.fakeRepository
import com.eazzyapps.test.domain.models.GitHubRepo
import com.eazzyapps.test.ui.viewmodels.DetailsViewModel
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun DetailsScreen(repo: GitHubRepo) {

    val vm = getViewModel<DetailsViewModel> { parametersOf(repo) }

    val commitsInfo by vm.commits.collectAsState()

    when (LocalConfiguration.current.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(16.dp),
            ) {
                RepoDetails(Modifier.fillMaxWidth(), vm.info)
                CommitsChart(commits = commitsInfo)
            }
        }
        else -> {

            val scrollState = rememberScrollState()

            Row(
                Modifier
                    .fillMaxSize()
                    .padding(16.dp),
            ) {
                RepoDetails(
                    Modifier
                        .fillMaxWidth(fraction = 0.5f)
                        .verticalScroll(scrollState), vm.info
                )
                CommitsChart(Modifier.fillMaxSize(), commits = commitsInfo)
            }
        }
    }

}

@Preview
@Composable
fun DetailsScreenPreview() {
    RepoDetails(Modifier, DetailsViewModel.Info(fakeRepository))
}

@Composable
fun RepoDetails(modifier: Modifier, info: DetailsViewModel.Info) {

    Column(
        Modifier
            .wrapContentHeight()
            .then(modifier)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            LabelValue(
                modifier = Modifier.fillMaxWidth(fraction = 0.5f),
                label = "Name",
                value = info.name
            )
            LabelValue(
                modifier = Modifier.fillMaxWidth(),
                label = "Id",
                value = info.id
            )
        }
        LabelValue(modifier = Modifier.fillMaxWidth(), label = "Owner", value = info.owner)
        Row(
            Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            LabelValue(
                modifier = Modifier.fillMaxWidth(fraction = 0.5f),
                label = "Created at",
                value = info.date
            )
            LabelValue(
                modifier = Modifier.fillMaxWidth(),
                label = "License",
                value = info.license
            )
        }
        LabelValue(modifier = Modifier.fillMaxWidth(), label = "Description", value = info.desc)
    }

}

@Composable
fun LabelValue(modifier: Modifier, label: String, value: String) {
    Column(
        modifier
            .wrapContentHeight()
            .padding(vertical = 8.dp)
    ) {
        Text(modifier = Modifier.fillMaxWidth(), text = label)
        Text(modifier = Modifier.fillMaxWidth(), text = value)
    }
}