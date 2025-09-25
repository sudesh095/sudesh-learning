package com.sudesh.data.repository

import com.sudesh.core.AppError
import com.sudesh.core.Resource
import com.sudesh.data.api.model.ArticleDto
import com.sudesh.data.datasource.LocalDataSource
import com.sudesh.data.datasource.RemoteDataSource
import com.sudesh.data.db.ArticleEntity
import com.sudesh.domain.model.Article
import com.sudesh.domain.repository.NewsRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

class NewsRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : BaseRepository(), NewsRepository {

    override fun getTopHeadlines(): Flow<Resource<List<Article>>> = flow {
        emit(Resource.Loading)

        // Step 1: Emit cached DB
        val cached = localDataSource.getArticlesOnce()
        if (cached.isNotEmpty()) {
            emit(Resource.Success(cached.map { it.toDomain() }))
        }

        // Step 2: Fetch from API with retry
        val response = safeApiCall {
            remoteDataSource.getTopHeadlines()
        }.retryWhen { _, attempt ->
            if (attempt < 3) {
                delay(1000L * (attempt + 1))
                true
            } else false
        }

        emitAll(response.map { res ->
            when (res) {
                is Resource.Success -> {
                    localDataSource.saveArticles(res.data.articles.map { it.toEntity() })
                    Resource.Success(res.data.articles.map { it.toDomain() })
                }
                is Resource.Error -> res
                is Resource.Loading -> Resource.Loading
            }
        })
    }.catch { e ->
        emit(Resource.Error(AppError.Unknown(e.message ?: "Unknown error")))
    }

    override fun searchNews(query: String): Flow<Resource<List<Article>>> =
        safeApiCall {
            remoteDataSource.searchNews(query)
        }.retryWhen { _, attempt ->
            if (attempt < 3) {
                delay(1000L * (attempt + 1))
                true
            } else false
        }.map { res ->
            when (res) {
                is Resource.Success -> Resource.Success(res.data.articles.map { it.toDomain() })
                is Resource.Error -> res
                is Resource.Loading -> Resource.Loading
            }
        }
}

// Mapping extensions
private fun ArticleDto.toEntity() = ArticleEntity(
    title = title,
    description = description,
    url = url,
    imageUrl = urlToImage,
    publishedAt = publishedAt
)

private fun ArticleDto.toDomain() = Article(
    id = url,
    title = title,
    description = description,
    url = url,
    imageUrl = urlToImage,
    publishedAt = publishedAt
)

private fun ArticleEntity.toDomain() = Article(
    id = url,
    title = title,
    description = description,
    url = url,
    imageUrl = imageUrl,
    publishedAt = publishedAt
)