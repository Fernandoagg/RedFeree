package com.example.redferee

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.redferee.ui.theme.RedFereeTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// --- COMPONENTES REUTILIZABLES ---

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
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant) // Color suave para la tarjeta
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = review.NombreArbi,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                RatingStars(review.Cali)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Text("Usuario", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                Spacer(modifier = Modifier.width(4.dp))
                Text(review.userName, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.width(16.dp))
                Text(review.tiempo.take(10), style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("Reseña:", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
            Text(review.Resena, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

// --- PANTALLA PRINCIPAL ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewsScreen(
    onBack: () -> Unit,
    onWriteReview: () -> Unit
) {
    // Estados
    var reviews by remember { mutableStateOf(emptyList<Review>()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val repository = remember { ReviewApiRepository() }

    // Carga de datos
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

    // CONTENEDOR PRINCIPAL (BOX) PARA EL FONDO
    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {

        // --- CÍRCULOS DECORATIVOS ---

        // 1. Círculo Azul (Más grande y atrás)
        Box(
            modifier = Modifier
                .size(200.dp) // Tamaño grande
                .offset(x = (0).dp, y = (-100).dp) // Desplazado hacia arriba-izquierda
                .clip(CircleShape)
                .background(Color(0xFFB3CDE0).copy(alpha = 0.9f)) // Azul claro semitransparente
        )

        // 2. Círculo Rojo/Rosa (Más pequeño y superpuesto)
        Box(
            modifier = Modifier
                .size(200.dp) // Tamaño mediano
                .offset(x = (-100).dp, y = (0).dp) // Desplazado más abajo
                .clip(CircleShape)
                .background(Color(0xFFE6A5B6).copy(alpha = 0.7f)) // Rosa semitransparente
        )

        // --- CONTENIDO DE LA PANTALLA (Scaffold Transparente) ---

        Scaffold(
            // Hacemos el fondo transparente para ver los círculos
            containerColor = Color.Transparent,

            topBar = {
                TopAppBar(
                    title = { Text("Calificaciones y Reseñas") },
                    // Barra transparente
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                        }
                    }
                )
            },
            bottomBar = {
                Surface(
                    color = Color.Transparent,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Button(
                        onClick = onWriteReview,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        // Color del botón (Azul oscuro para contraste)
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4F5D75))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(
                            text = "Escribir Reseña",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        ) { paddingValues ->

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
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    contentPadding = paddingValues,
                    verticalArrangement = Arrangement.spacedBy(12.dp) // Un poco más de espacio entre tarjetas
                ) {
                    items(reviews) { review ->
                        ReviewItem(review)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReviewsScreenPreview() {
    RedFereeTheme {
        ReviewsScreen(onBack = {}, onWriteReview = {})
    }
}