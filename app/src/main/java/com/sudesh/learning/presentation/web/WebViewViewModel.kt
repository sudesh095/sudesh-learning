package com.sudesh.learning.presentation.web

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class WebViewViewModel @Inject constructor(): ViewModel() {
    private val _progress = MutableStateFlow(0)
    val progress: StateFlow<Int> = _progress.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _title = MutableStateFlow("Web Page")
    val title: StateFlow<String> = _title.asStateFlow()

    fun updateProgress(newProgress: Int) {
        _progress.value = newProgress
        _isLoading.value = newProgress < 100
    }

    fun setTitle(newTitle: String) {
        _title.value = newTitle
    }

    fun setLoading(loading: Boolean) {
        _isLoading.value = loading
    }
}