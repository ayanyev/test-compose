package com.eazzyapps.test.repositories.ui

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.eazzyapps.test.MainActivity
import com.eazzyapps.test.R
import com.eazzyapps.repositories.domain.fakeRepoViewModelsList
import com.eazzyapps.repositories.domain.fakeRepositoryViewModel
import com.eazzyapps.repositories.ui.composables.RepoItem
import com.eazzyapps.repositories.ui.composables.ReposList
import com.eazzyapps.repositories.ui.theme.AppTheme
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