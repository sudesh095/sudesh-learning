package com.sudesh.learning.presentation.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sudesh.core.repository.NetworkMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val networkMonitor: NetworkMonitor
) : ViewModel() {

    private val _isLowInternet = MutableStateFlow(false)
    val isLowInternet: StateFlow<Boolean> = _isLowInternet

    fun monitorNetwork() {
        viewModelScope.launch {
            while (true) {
                _isLowInternet.value = !networkMonitor.isConnected()
                delay(3000)
            }
        }
    }
}