package com.sudesh.learning.ui

import com.sudesh.domain.model.Article

sealed class UiState {
    object Loading : UiState()
    data class Success(val articles: List<Article>) : UiState()
    object Empty : UiState()
    data class Error(val message: String) : UiState()
}