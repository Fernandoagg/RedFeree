package com.example.redferee

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.redferee.ui.theme.RedFereeTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// --- COMPONENTES REUTILIZABLES DE UI ---

@Composable
fun RatingStars(rating: Int) {
    Row {
        for (i in 1..5) {
            val star = if (i <= rating) "★" else "☆"
            Text(
                text = star,
                color = if (i <= rating) Color(0xFFFFA500) else Color.Gray,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = 1.dp)
            )
        }
    }
}

@Composable
fun ReviewItem(review: Review) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = review.NombreArbi,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                RatingStars(review.Cali)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row {
                Text("Usuario", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                Spacer(modifier = Modifier.width(4.dp))
                Text(review.userName, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
                Spacer(modifier = Modifier.width(16.dp))
                // Mostrar fecha tal cual viene de la API (o formatearla si prefieres)
                Text(review.tiempo.take(10), style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text("Reseña:", style = MaterialTheme.typography.bodyMedium)
            Text(review.Resena, style = MaterialTheme.typography.bodyMedium, maxLines = 3)
        }
    }
}

// --- PANTALLA PRINCIPAL ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewsScreen(onBack: () -> Unit) {
    // 1. Estados para almacenar los datos
    var reviews by remember { mutableStateOf(emptyList<Review>()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val repository = remember { ReviewApiRepository() }

    // 2. Carga de datos
    LaunchedEffect(Unit) {
        val result = withContext(Dispatchers.IO) {
            repository.fetchAllReviews()
        }
        isLoading = false
        if (result.isSuccess) {
            reviews = result.getOrThrow()
        } else {
            errorMessage = "Error: ${result.exceptionOrNull()?.message}"
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Fondo Decorativo
        Box(modifier = Modifier.size(150.dp).offset((-30).dp, (-30).dp).clip(CircleShape).background(Color(0x003159).copy(alpha = 0.5f)))
        Box(modifier = Modifier.size(150.dp).offset((-80).dp, 40.dp).clip(CircleShape).background(Color(0xFF203A).copy(alpha = 0.5f)))

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { Text("Calificaciones y Reseñas") },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                        }
                    }
                )
            }
        ) { paddingValues ->

            // 3. Lógica de UI basada en el estado
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (errorMessage != null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(errorMessage!!, color = Color.Red)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                    contentPadding = paddingValues,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // AQUÍ ESTÁ LA CORRECCIÓN CLAVE:
                    // Usamos 'items(reviews)' para iterar sobre la lista real de la API.
                    items(reviews) { review ->
                        ReviewItem(review)
                    }
                }
            }
        }
    }
}

// --- ACTIVITY ---

class CalisReseActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RedFereeTheme {
                ReviewsScreen(onBack = {})
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReviewsScreenPreview() {
    RedFereeTheme {
        ReviewsScreen(onBack = {})
    }
}