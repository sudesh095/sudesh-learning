package com.sudesh.learning.presentation.news

import app.cash.turbine.test
import com.sudesh.core.AppError
import com.sudesh.core.Resource
import com.sudesh.learning.ui.UiState
import com.sudesh.domain.model.Article
import com.sudesh.domain.usecase.GetTopHeadlinesUseCase
import com.sudesh.domain.usecase.SearchNewsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class NewsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var getTopHeadlines: GetTopHeadlinesUseCase
    private lateinit var searchNews: SearchNewsUseCase
    private lateinit var viewModel: NewsViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getTopHeadlines = mock()
        searchNews = mock()
        viewModel = NewsViewModel(getTopHeadlines, searchNews)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should emit Success when articles available`() = runTest {
        val articles = listOf(Article("1", "Title", "Desc", "url", "img", "2024-01-01"))
        whenever(getTopHeadlines()).thenReturn(flowOf(Resource.Success(articles)))

        viewModel.fetchHeadlines()

        viewModel.state.test {
            assert(awaitItem() is UiState.Loading)
            assert(awaitItem() is UiState.Success)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should emit Empty when no data`() = runTest {
        whenever(getTopHeadlines()).thenReturn(flowOf(Resource.Success(emptyList())))

        viewModel.fetchHeadlines()

        viewModel.state.test {
            assert(awaitItem() is UiState.Loading)
            assert(awaitItem() is UiState.Empty)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should emit Error when repo fails`() = runTest {
        whenever(getTopHeadlines()).thenReturn(flowOf(Resource.Error(AppError.Unknown("Boom"))))

        viewModel.fetchHeadlines()

        viewModel.state.test {
            assert(awaitItem() is UiState.Loading)
            assert(awaitItem() is UiState.Error)
            cancelAndIgnoreRemainingEvents()
        }
    }
}