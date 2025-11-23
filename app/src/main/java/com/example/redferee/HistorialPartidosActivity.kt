package com.example.redferee

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class HistorialPartidosActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HistorialPartidosScreen(
                onBack = { finish() }
            )
        }
    }
}
