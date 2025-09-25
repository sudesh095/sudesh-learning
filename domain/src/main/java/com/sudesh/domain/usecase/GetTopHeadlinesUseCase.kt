package com.sudesh.domain.usecase

import com.sudesh.core.Resource
import com.sudesh.domain.model.Article
import com.sudesh.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow

class GetTopHeadlinesUseCase(
    private val repository: NewsRepository
) {
    operator fun invoke(): Flow<Resource<List<Article>>> = repository.getTopHeadlines()
}