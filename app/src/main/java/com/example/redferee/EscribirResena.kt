package com.example.redferee

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.redferee.ui.theme.RedFereeTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WriteReviewScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Instancia del repositorio
    val repository = remember { ReviewApiRepository() }

    // ESTADOS
    var referees by remember { mutableStateOf(emptyList<Referee>()) }
    var selectedReferee by remember { mutableStateOf<Referee?>(null) }
    var expandedDropdown by remember { mutableStateOf(false) }
    var rating by remember { mutableIntStateOf(0) }
    var reviewText by remember { mutableStateOf("") }
    var isSubmitting by remember { mutableStateOf(false) }

    // Cargar árbitros
    LaunchedEffect(Unit) {
        val result = withContext(Dispatchers.IO) { repository.fetchReferees() }
        if (result.isSuccess) {
            referees = result.getOrThrow()
        } else {
            Toast.makeText(context, "Error al cargar la lista de árbitros", Toast.LENGTH_LONG).show()
        }
    }

    // --- FONDO CON CÍRCULOS DECORATIVOS ---
    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {

        // 1. Círculo Azul
        Box(
            modifier = Modifier
                .size(200.dp)
                .offset(x = (0).dp, y = (-100).dp)
                .clip(CircleShape)
                .background(Color(0xFFB3CDE0).copy(alpha = 0.7f))
        )

        // 2. Círculo Rosa
        Box(
            modifier = Modifier
                .size(200.dp)
                .offset(x = (-100).dp, y = (0).dp)
                .clip(CircleShape)
                .background(Color(0xFFE6A5B6).copy(alpha = 0.8f))
        )

        // --- CONTENIDO ---
        Scaffold(
            containerColor = Color.Transparent, // Transparente para ver los círculos
            topBar = {
                TopAppBar(
                    title = { Text("Escribir Reseña") },
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
                        onClick = {
                            if (selectedReferee != null && rating > 0 && reviewText.isNotEmpty()) {
                                isSubmitting = true

                                scope.launch {
                                    val request = ReviewRequest(
                                        arbitroid = selectedReferee!!.id,
                                        usuarioid = 2, // ID fijo como pediste en tu código
                                        estrellas = rating,
                                        texto = reviewText
                                    )

                                    val result = withContext(Dispatchers.IO) {
                                        repository.submitReview(request)
                                    }

                                    isSubmitting = false

                                    if (result.isSuccess) {
                                        Toast.makeText(context, "¡Reseña publicada con éxito!", Toast.LENGTH_LONG).show()
                                        onBack()
                                    } else {
                                        val errorMsg = result.exceptionOrNull()?.message ?: "Error desconocido"
                                        Toast.makeText(context, "Error al enviar: $errorMsg", Toast.LENGTH_LONG).show()
                                    }
                                }
                            } else {
                                Toast.makeText(context, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4F5D75)), // Color oscuro del botón
                        enabled = !isSubmitting
                    ) {
                        if (isSubmitting) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                        } else {
                            Text("Subir Reseña", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {

                // Spinner
                Text("Selecciona al Árbitro", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))

                ExposedDropdownMenuBox(
                    expanded = expandedDropdown,
                    onExpandedChange = { expandedDropdown = !expandedDropdown }
                ) {
                    OutlinedTextField(
                        value = selectedReferee?.nombre ?: "Seleccionar...",
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDropdown) },
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White.copy(alpha = 0.9f), // Fondo blanco para leer bien sobre los círculos
                            unfocusedContainerColor = Color.White.copy(alpha = 0.9f)
                        )
                    )

                    ExposedDropdownMenu(
                        expanded = expandedDropdown,
                        onDismissRequest = { expandedDropdown = false }
                    ) {
                        if (referees.isEmpty()) {
                            DropdownMenuItem(
                                text = { Text("Cargando árbitros...") },
                                onClick = { }
                            )
                        } else {
                            referees.forEach { referee ->
                                DropdownMenuItem(
                                    text = { Text(referee.nombre) },
                                    onClick = {
                                        selectedReferee = referee
                                        expandedDropdown = false
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Estrellas
                Text("Calificación", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    for (i in 1..5) {
                        val color = if (i <= rating) Color(0xFFFFA500) else Color.LightGray
                        Text(
                            text = "★",
                            color = color,
                            fontSize = 40.sp,
                            modifier = Modifier
                                .clickable { rating = i }
                                .padding(end = 8.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // TextBox
                Text("Tu opinión", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = reviewText,
                    onValueChange = { reviewText = it },
                    label = { Text("Escribe aquí tu experiencia...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    maxLines = 10,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White.copy(alpha = 0.9f), // Fondo blanco para leer bien
                        unfocusedContainerColor = Color.White.copy(alpha = 0.9f)
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun WriteReviewScreenPreview() {
    RedFereeTheme {
        WriteReviewScreen(onBack = {})
    }
}