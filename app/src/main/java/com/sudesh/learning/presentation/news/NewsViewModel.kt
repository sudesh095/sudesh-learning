package com.sudesh.learning.presentation.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sudesh.core.Resource
import com.sudesh.learning.ui.UiState
import com.sudesh.domain.usecase.GetTopHeadlinesUseCase
import com.sudesh.domain.usecase.SearchNewsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val getTopHeadlines: GetTopHeadlinesUseCase,
    private val searchNews: SearchNewsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<UiState>(UiState.Loading)
    val state: StateFlow<UiState> = _state.asStateFlow()

    private val _searchQuery = MutableStateFlow("")

    init {
        fetchHeadlines()
        observeSearch()
    }

    fun fetchHeadlines() {
        viewModelScope.launch {
            getTopHeadlines()
                .collect { resource ->
                    when (resource) {
                        is Resource.Loading -> _state.value = UiState.Loading
                        is Resource.Success -> {
                            _state.value = if (resource.data.isEmpty())
                                UiState.Empty
                            else UiState.Success(resource.data)
                        }
                        is Resource.Error -> _state.value = UiState.Error(resource.error.message)
                    }
                }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    @OptIn(FlowPreview::class)
    private fun observeSearch() {
        _searchQuery
            .debounce(300)
            .filter { if(it.isEmpty()) {
                fetchHeadlines()
                false
            } else{
                it.isNotEmpty()
            } }
            .onEach { query ->
                search(query)
            }
            .launchIn(viewModelScope)
    }

    private fun search(query: String) {
        viewModelScope.launch {
            searchNews(query)
                .collect { resource ->
                    when (resource) {
                        is Resource.Loading -> _state.value = UiState.Loading
                        is Resource.Success -> {
                            _state.value = if (resource.data.isEmpty())
                                UiState.Empty
                            else UiState.Success(resource.data)
                        }
                        is Resource.Error -> _state.value = UiState.Error(resource.error.message)
                    }
                }
        }
    }
}