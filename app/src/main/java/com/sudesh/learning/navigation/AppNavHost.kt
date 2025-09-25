package com.sudesh.learning.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sudesh.core.Constants
import com.sudesh.learning.presentation.news.NewsScreenWrapper
import com.sudesh.learning.presentation.news.NewsViewModel
import com.sudesh.learning.presentation.player.PlayerScreen
import com.sudesh.learning.presentation.web.WebViewScreen
import com.sudesh.domain.model.Article
import java.net.URLDecoder
import java.net.URLEncoder

@Composable
fun AppNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavRoutes.NEWS,
        modifier = modifier
    ) {
        composable(NavRoutes.NEWS) {
            val vm: NewsViewModel = hiltViewModel()
            NewsScreenWrapper(
                viewModel = vm,
                onArticleClick = { article: Article ->
                    navController.navigate(NavRoutes.WEB + "/" + URLEncoder.encode(article.url ?: "", "UTF-8"))
                }
            )
        }

        composable(
            route = NavRoutes.WEB + "/{url}",
            arguments = listOf(navArgument("url") { type = NavType.StringType })
        ) { backStackEntry ->
            val encoded = backStackEntry.arguments?.getString("url") ?: Constants.VIDEO_URL
            val url = URLDecoder.decode(encoded, "UTF-8")
            WebViewScreen(url = url, onBackPressed = {
                navController.popBackStack()
            })
        }

        composable(
            route = NavRoutes.PLAYER + "/{url}",
            arguments = listOf(navArgument("url") { type = NavType.StringType })
        ) { backStackEntry ->
            val encoded = backStackEntry.arguments?.getString("url") ?: Constants.VIDEO_URL
            val url = URLDecoder.decode(encoded, "UTF-8")
            PlayerScreen(url = url)
        }
    }
}