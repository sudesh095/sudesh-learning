package com.sudesh.learning.presentation.news

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.sudesh.learning.ui.UiState
import org.junit.Rule
import org.junit.Test

class NewsScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun showsLoadingState() {
        composeRule.setContent {
            NewsScreen(
                state = UiState.Loading,
                onSearch = {},
                onRetry = {},
                onArticleClick = {}
            )
        }
        composeRule.onNodeWithTag("loading").assertIsDisplayed()
    }

    @Test
    fun showsErrorState() {
        composeRule.setContent {
            NewsScreen(
                state = UiState.Error("Boom"),
                onSearch = {},
                onRetry = {},
                onArticleClick = {}
            )
        }
        composeRule.onNodeWithText("Error: Boom").assertIsDisplayed()
        composeRule.onNodeWithText("Retry").assertIsDisplayed()
    }

    @Test
    fun showsSuccessList() {
        val articles = listOf(Article("1","Title","Desc","url","img","2024-01-01"))
        composeRule.setContent {
            NewsScreen(
                state = UiState.Success(articles),
                onSearch = {},
                onRetry = {},
                onArticleClick = {}
            )
        }
        composeRule.onNodeWithText("Title").assertIsDisplayed()
    }
}

