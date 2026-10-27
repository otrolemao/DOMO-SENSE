package com.example.domosense

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.domosense.data.preferences.SessionType
import com.example.domosense.data.preferences.UserPreferences
import com.example.domosense.ui.screens.LoginScreen
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
    val scope = rememberCoroutineScope()

    val onboardingCompletado by userPreferences.onboardingCompletado.collectAsState(initial = null)
    val sessionType by userPreferences.sessionType.collectAsState(initial = null)

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { _ -> }

    LaunchedEffect(key1 = sessionType) {
        if (sessionType != SessionType.NONE && sessionType != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    if (onboardingCompletado == null || sessionType == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Cargando...", color = MaterialTheme.colorScheme.primary)
        }
        return
    }

    when {
        onboardingCompletado == false -> {
            OnboardingScreen(
                onFinish = { scope.launch { userPreferences.marcarOnboardingCompletado() } }
            )
        }
        sessionType == SessionType.NONE -> {
            LoginScreen(
                onLoginSuccess = { scope.launch { userPreferences.guardarSession(SessionType.REGISTERED) } },
                onGuestLogin = { scope.launch { userPreferences.guardarSession(SessionType.GUEST) } }
            )
        }
        else -> {
            MainScreen(
                isGuest = sessionType == SessionType.GUEST,
                onCerrarSession = { scope.launch { userPreferences.cerrarSession() } }
            )
        }
    }
}