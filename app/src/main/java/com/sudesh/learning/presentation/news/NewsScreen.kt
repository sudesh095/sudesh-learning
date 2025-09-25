package com.sudesh.learning.presentation.news

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sudesh.learning.ui.UiState
import com.sudesh.learning.ui.common.EmptyView
import com.sudesh.learning.ui.common.ErrorView
import com.sudesh.learning.ui.common.LoadingView
import com.sudesh.domain.model.Article

// 🔹 Pure UI Composable (no ViewModel inside)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsScreen(
    state: UiState,
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    onRetry: () -> Unit,
    contentColor : Color = MaterialTheme.colorScheme.primary,
    onArticleClick: (Article) -> Unit
) {
    Scaffold(
        containerColor = contentColor,
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = contentColor,
                    titleContentColor = Color.White
                ),
                title = { Text("News Headlines", style = TextStyle(color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.W600)) }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .background(color = contentColor)
                .padding(top = padding.calculateTopPadding(), bottom = padding.calculateBottomPadding())
                .fillMaxSize()
        ) {
            HorizontalDivider(Modifier.height(1.dp), thickness = 1.dp, color = Color.White.copy(alpha = 0.8f))
            OutlinedTextField(
                value = searchQuery,                 // <- use directly
                onValueChange = onSearchChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .testTag("searchField"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.White.copy(alpha = 0.8f),      // outline when focused
                    unfocusedBorderColor = Color.Gray,    // outline when not focused
                    disabledBorderColor = Color.LightGray,// outline when disabled
                    errorBorderColor = Color.Red,         // outline when error
                    cursorColor = Color.White,          // cursor color
                    focusedLabelColor = Color.White.copy(alpha = 0.8f),       // label when focused
                    unfocusedLabelColor = Color.Gray,     // label when not focused
                    errorLabelColor = Color.Red,          // label when error
                    focusedTextColor = Color.White,       // text when focused
                    unfocusedTextColor = Color.DarkGray,  // text when not focused
                    errorTextColor = Color.Red            // text when error
                ),
                shape = RoundedCornerShape(16.dp),
                textStyle = TextStyle(color = Color.White.copy(alpha = 0.6f), fontSize = 18.sp, fontWeight = FontWeight.W600),
                placeholder = { Text("Search news...",style = TextStyle(color = Color.White.copy(alpha = 0.6f), fontSize = 15.sp, fontWeight = FontWeight.W400)) },
                singleLine = true
            )

            Column(Modifier.fillMaxSize().background(color = Color.Gray.copy(alpha = 0.6f))) {
                when (state) {
                    is UiState.Loading -> LoadingView()
                    is UiState.Success -> NewsList(state.articles, onArticleClick)
                    is UiState.Empty -> EmptyView()
                    is UiState.Error -> ErrorView(
                        message = state.message,
                        onRetry = onRetry
                    )
                }
            }
        }
    }
}

// 🔹 Wrapper that connects ViewModel to UI
@Composable
fun NewsScreenWrapper(
    viewModel: NewsViewModel,
    onArticleClick: (Article) -> Unit
) {
    val state by viewModel.state.collectAsState()
    var searchQuery by remember { mutableStateOf("") }


    NewsScreen(
        state = state,
        searchQuery = searchQuery,
        onSearchChange = {
            searchQuery = it
            viewModel.onSearchQueryChanged(it)
        },
        onRetry = { viewModel.fetchHeadlines() },
        onArticleClick = onArticleClick
    )
}