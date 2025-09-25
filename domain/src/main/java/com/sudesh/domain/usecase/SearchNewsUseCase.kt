package com.sudesh.domain.usecase

import com.sudesh.core.Resource
import com.sudesh.domain.model.Article
import com.sudesh.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow

class SearchNewsUseCase(
    private val repository: NewsRepository
) {
    operator fun invoke(query: String): Flow<Resource<List<Article>>> =
        repository.searchNews(query)
}