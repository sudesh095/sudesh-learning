package com.sudesh.learning

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.sudesh.learning.navigation.AppNavHost
import com.sudesh.learning.ui.theme.SudeshMvvmTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Install splash before super.onCreate
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)

        // Optional: keep splash until data is ready
        splashScreen.setKeepOnScreenCondition {
            // e.g., keep splash until ViewModel says loading is false
            false // change to "true" if you want to delay hiding
        }
        enableEdgeToEdge()
        setContent {
            SudeshMvvmTheme {
                Surface {
                    AppNavHost()
                }
            }
        }
    }
}
