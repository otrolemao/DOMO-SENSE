package com.example.domosense

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.domosense.data.preferences.UserPreferences
import com.example.domosense.ui.screens.MainScreen
import com.example.domosense.ui.screens.OnboardingScreen
import com.example.domosense.ui.theme.DOMOSENSETheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DOMOSENSETheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DOMOSENSEApp()
                }
            }
        }
    }
}

@Composable
fun DOMOSENSEApp() {
    val context = LocalContext.current
    val userPreferences = UserPreferences(context)
    val onboardingCompletado by userPreferences.onboardingCompletado
        .collectAsState(initial = null)
    val scope = rememberCoroutineScope()

    if (onboardingCompletado == null) {
        Box(modifier = Modifier.fillMaxSize())
        return
    }

    if (onboardingCompletado == true) {
        MainScreen()
    } else {
        OnboardingScreen(
            onFinish = {
                scope.launch {
                    userPreferences.marcarOnboardingCompletado()
                }
            }
        )
    }
}