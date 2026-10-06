package com.example.domosense

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.domosense.ui.screens.MainScreen
import com.example.domosense.ui.theme.DOMOSENSETheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DOMOSENSETheme {
                MainScreen()
            }
        }
    }
}