package com.eazzyapps.repositories.ui.composables

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eazzyapps.repositories.R
import com.eazzyapps.repositories.ui.viewmodels.MonthViewModel

@Composable
fun CommitsChart(modifier: Modifier = Modifier, commits: List<MonthViewModel>) {
    Column(modifier = modifier) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            textAlign = TextAlign.Center,
            text = stringResource(id = R.string.commits_history_title).uppercase()
        )
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {
            for (c in commits) {
                Month(c)
            }
        }
    }
}

@Composable
fun Month(vm: MonthViewModel) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(72.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            textAlign = TextAlign.Center,
            text = vm.count.toString()
        )
        BoxWithConstraints {

            val height = (maxHeight - 40.dp) / vm.maxCount * vm.count

            Box(
                Modifier
                    .background(MaterialTheme.colors.onSurface)
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .animateContentSize()
                    .height(height)

            )
        }
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            textAlign = TextAlign.Center,
            text = vm.month
        )
    }
}

@Preview
@Composable
fun CommitsChartPreview() {
    CommitsChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        commits = commits
    )
}

internal val commits = listOf(
    MonthViewModel(20, 4, "Jan.21"),
    MonthViewModel(20, 5, "Jan.21"),
    MonthViewModel(20, 20, "Jan.21"),
)