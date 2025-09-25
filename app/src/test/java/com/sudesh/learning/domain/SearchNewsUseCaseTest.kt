package com.sudesh.learning.domain

import app.cash.turbine.test
import com.sudesh.core.AppError
import com.sudesh.core.Resource
import com.sudesh.domain.model.Article
import com.sudesh.domain.repository.NewsRepository
import com.sudesh.domain.usecase.SearchNewsUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class SearchNewsUseCaseTest {

    private val repo: NewsRepository = mock()
    private val useCase = SearchNewsUseCase(repo)

    @Test
    fun `returns Success when repo provides articles`() = runTest {
        val articles = listOf(Article("1", "QueryTitle", "Desc", "url", "img", "2024-01-01"))
        whenever(repo.searchNews("query")).thenReturn(flowOf(Resource.Success(articles)))

        useCase("query").test {
            assert(awaitItem() is Resource.Success)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `returns Error when repo fails`() = runTest {
        whenever(repo.searchNews("query")).thenReturn(flowOf(Resource.Error(AppError.Unknown("Boom"))))

        useCase("query").test {
            assert(awaitItem() is Resource.Error)
            cancelAndConsumeRemainingEvents()
        }
    }
}