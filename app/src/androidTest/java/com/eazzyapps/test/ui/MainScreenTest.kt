package com.eazzyapps.test.ui

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.eazzyapps.test.MainActivity
import com.eazzyapps.test.R
import com.eazzyapps.test.data.fakeRepoViewModelsList
import com.eazzyapps.test.data.fakeRepositoryViewModel
import com.eazzyapps.test.ui.composables.RepoItem
import com.eazzyapps.test.ui.composables.ReposList
import com.eazzyapps.test.ui.theme.AppTheme
import org.junit.Rule
import org.junit.Test

class MainScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun repoListNotEmptyTest() {
        composeTestRule.setContent {
            AppTheme {
                ReposList(fakeRepoViewModelsList)
            }
        }

        with(composeTestRule.onNodeWithTag("repoList")) {
            onChildren().assertCountEquals(fakeRepoViewModelsList.size)
            onChildren().assertAll(hasClickAction())
        }

        Thread.sleep(1000)

    }

    @Test
    fun repoItemContentTest() {
        composeTestRule.setContent {
            AppTheme {
                RepoItem(fakeRepositoryViewModel)
            }
        }

        val resources = composeTestRule.activity.resources

        with(composeTestRule.onNodeWithTag("repoItem")) {
            printToLog("repoItem")
            assert(
                hasContentDescriptionExactly(
                    resources.getString(R.string.cont_desc_icon_watchers),
                    resources.getString(R.string.cont_desc_icon_stars),
                    resources.getString(R.string.cont_desc_icon_forks)
                )
            )
            assert(hasText(fakeRepositoryViewModel.name))
                .assert(hasText(fakeRepositoryViewModel.desc))
                .assert(hasText(fakeRepositoryViewModel.watchersCount))
                .assert(hasText(fakeRepositoryViewModel.starsCount))
                .assert(hasText(fakeRepositoryViewModel.forksCount))
                .assertHasClickAction()
        }

        Thread.sleep(1000)

    }
}