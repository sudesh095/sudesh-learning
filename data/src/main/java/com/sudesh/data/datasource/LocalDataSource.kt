package com.sudesh.data.datasource

import com.sudesh.data.db.ArticleDao
import com.sudesh.data.db.ArticleEntity

class LocalDataSource(
    private val dao: ArticleDao
) {
    suspend fun getArticlesOnce(): List<ArticleEntity> {
        return dao.getArticlesOnce()
    }

    suspend fun saveArticles(articles: List<ArticleEntity>) {
        dao.clearAll()
        dao.insertArticles(articles)
    }

    suspend fun clearArticles() {
        dao.clearAll()
    }
}