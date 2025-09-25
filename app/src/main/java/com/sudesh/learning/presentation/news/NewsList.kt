package com.sudesh.learning.presentation.news

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sudesh.domain.model.Article
import kotlin.random.Random

@Composable
fun NewsList(articles: List<Article>, onArticleClick: (Article) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(articles, key = { article ->  article.id.plus(Random.nextInt()) } ) { article ->
            NewsItem(article, onClick = { onArticleClick(article) })
        }
    }
}
