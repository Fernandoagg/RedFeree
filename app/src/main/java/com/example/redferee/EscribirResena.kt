package com.example.redferee

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
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

// --- PANTALLA DE FORMULARIO ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WriteReviewScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Instancia del repositorio para conectar con la API
    val repository = remember { ReviewApiRepository() }

    // ESTADOS DE LA PANTALLA
    // Lista de árbitros cargada desde la API
    var referees by remember { mutableStateOf(emptyList<Referee>()) }
    // Árbitro seleccionado por el usuario
    var selectedReferee by remember { mutableStateOf<Referee?>(null) }
    // Control del menú desplegable
    var expandedDropdown by remember { mutableStateOf(false) }
    // Calificación seleccionada (1-5)
    var rating by remember { mutableIntStateOf(0) }
    // Texto de la reseña
    var reviewText by remember { mutableStateOf("") }
    // Estado de carga al enviar
    var isSubmitting by remember { mutableStateOf(false) }

    // EFECTO: Cargar la lista de árbitros al iniciar la pantalla
    LaunchedEffect(Unit) {
        val result = withContext(Dispatchers.IO) { repository.fetchReferees() }
        if (result.isSuccess) {
            referees = result.getOrThrow()
        } else {
            Toast.makeText(context, "Error al cargar la lista de árbitros", Toast.LENGTH_LONG).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Escribir Reseña") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        },
        bottomBar = {
            // BOTÓN DE ENVÍO (Fijado en la parte inferior)
            Button(
                onClick = {
                    // Validación básica de campos
                    if (selectedReferee != null && rating > 0 && reviewText.isNotEmpty()) {
                        isSubmitting = true

                        scope.launch {
                            // Creamos el objeto de petición
                            // NOTA: 'usuarioid' está fijo en 2 para pruebas.
                            val request = ReviewRequest(
                                arbitroid = selectedReferee!!.id,
                                usuarioid = 2,
                                estrellas = rating,
                                texto = reviewText
                            )

                            // Llamada a la API en segundo plano
                            val result = withContext(Dispatchers.IO) {
                                repository.submitReview(request)
                            }

                            isSubmitting = false

                            if (result.isSuccess) {
                                Toast.makeText(context, "¡Reseña publicada con éxito!", Toast.LENGTH_LONG).show()
                                onBack() // Regresamos a la pantalla anterior si fue exitoso
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
                    .padding(16.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = !isSubmitting // Deshabilitar el botón mientras se envía
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Subir Reseña", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    ) { paddingValues ->
        // CONTENIDO DEL FORMULARIO
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {

            // 1. SELECCIÓN DE ÁRBITRO (Spinner / Dropdown)
            Text("Selecciona al Árbitro", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            ExposedDropdownMenuBox(
                expanded = expandedDropdown,
                onExpandedChange = { expandedDropdown = !expandedDropdown }
            ) {
                OutlinedTextField(
                    value = selectedReferee?.nombre ?: "Seleccionar...",
                    onValueChange = {},
                    readOnly = true, // El usuario solo puede seleccionar de la lista
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDropdown) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
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

            // 2. SELECCIÓN DE ESTRELLAS (Interactivo)
            Text("Calificación", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                for (i in 1..5) {
                    val color = if (i <= rating) Color(0xFFFFA500) else Color.LightGray // Color oro o gris
                    Text(
                        text = "★",
                        color = color,
                        fontSize = 40.sp,
                        modifier = Modifier
                            .clickable { rating = i } // Actualiza la calificación al hacer clic
                            .padding(end = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 3. CAMPO DE TEXTO (Reseña)
            Text("Tu opinión", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = reviewText,
                onValueChange = { reviewText = it },
                label = { Text("Escribe aquí tu experiencia...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                maxLines = 10
            )
        }
    }
}

// --- VISTA PREVIA ---

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun WriteReviewScreenPreview() {
    RedFereeTheme {
        WriteReviewScreen(onBack = {})
    }
}