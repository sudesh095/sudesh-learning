package com.sudesh.domain.repository

import com.sudesh.core.Resource
import com.sudesh.domain.model.Article
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    fun getTopHeadlines(): Flow<Resource<List<Article>>>
    fun searchNews(query: String): Flow<Resource<List<Article>>>
}