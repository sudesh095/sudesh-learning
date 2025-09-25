package com.sudesh.data.datasource

import com.sudesh.core.Constants
import com.sudesh.data.api.NewsApi
import com.sudesh.data.api.model.NewsResponse

class RemoteDataSource(
    private val api: NewsApi
) {
    suspend fun getTopHeadlines(): NewsResponse {
        return api.getTopHeadlines(apiKey = Constants.API_KEY)
    }

    suspend fun searchNews(query: String): NewsResponse {
        return api.searchNews(query, Constants.API_KEY)
    }
}