package com.example.redferee

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch

// ---------------------------------------------------------
// 1. MODELO DE DATOS Y VIEWMODEL
// ---------------------------------------------------------

data class Partido(
    val id: Int,
    val nombreArbitro: String,
    val costo: String
)

class HistorialViewModel : ViewModel() {
    var listaPartidos by mutableStateOf(listOf<Partido>())
        private set

    init {
        obtenerPartidosDelBackend()
    }

    fun obtenerPartidosDelBackend() {
        viewModelScope.launch {
            try {
                // LLAMADA AL SERVIDOR (Node.js)
                // Asegúrate de que en RetrofitClient la ruta sea @GET("api/partidos")
                val respuestaBackend = RetrofitClient.apiService.obtenerPartidos()

                // MAPEO: Convertimos los datos JSON a tu modelo visual
                listaPartidos = respuestaBackend.map { item ->
                    Partido(
                        id = item.id,
                        // Si el backend no manda nombre, ponemos uno por defecto
                        nombreArbitro = item.nombreArbitro ?: "Árbitro asignado",
                        // Si el backend no manda costo, ponemos "Pendiente"
                        costo = item.costo ?: "Pendiente"
                    )
                }
            } catch (e: Exception) {
                Log.e("API_ERROR", "Error conectando a Node: ${e.message}")
                // Mostrar error visual en la lista si falla la red
                listaPartidos = listOf(
                    Partido(0, "Error de Conexión", "Revisa tu red")
                )
            }
        }
    }
}

// ---------------------------------------------------------
// 2. CONSTANTES DE DISEÑO
// ---------------------------------------------------------
private val BlueDark = Color(0xFF0C2847)
private val RedAccent = Color(0xFFD33A35)
private val LightGrayBg = Color(0xFFE8ECEF)

// ---------------------------------------------------------
// 3. PANTALLA PRINCIPAL
// ---------------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistorialPartidosScreen(
    onBack: () -> Unit,
    viewModel: HistorialViewModel = viewModel()
) {
    val partidos = viewModel.listaPartidos

    Column {
        // Barra superior
        TopAppBar(
            title = { Text("Historial de Árbitros") },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Regresar"
                    )
                }
            }
        )

        // Contenido principal con Fondo
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(LightGrayBg)
        ) {
            // --- DECORACIÓN DE FONDO ---
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .offset(x = (0).dp, y = (-100).dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE6A5B6).copy(alpha = 0.7f))
            )

            Box(
                modifier = Modifier
                    .size(200.dp)
                    .offset(x = (-100).dp, y = (0).dp)
                    .clip(CircleShape)
                    .background(Color(0xFFB3CDE0).copy(alpha = 0.9f))
            )

            // --- LISTA DINÁMICA ---
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(top = 20.dp, bottom = 20.dp)
            ) {

                // Espaciador inicial
                item { Spacer(modifier = Modifier.height(20.dp)) }

                // Lista de partidos
                items(partidos) { partido ->
                    RefereeCard(name = partido.nombreArbitro, price = partido.costo)
                    Spacer(modifier = Modifier.height(18.dp))
                }

                // Footer (Logo y Paginación)
                item {
                    Spacer(modifier = Modifier.height(20.dp))

                    Image(
                        painter = painterResource(id = R.drawable.redfere), // Asegúrate de tener esta imagen o cambia el nombre
                        contentDescription = "Logo",
                        modifier = Modifier
                            .height(150.dp)
                            .padding(10.dp),
                        contentScale = ContentScale.Fit
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "◀", fontSize = 28.sp, color = BlueDark)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Página 1",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = BlueDark
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text = "▶", fontSize = 28.sp, color = BlueDark)
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}

// ---------------------------------------------------------
// 4. TARJETA INDIVIDUAL (Con corrección de precio)
// ---------------------------------------------------------
@Composable
fun RefereeCard(name: String, price: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(2.dp, BlueDark),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Círculo con inicial
            Box(
                modifier = Modifier
                    .size(45.dp)
                    .clip(CircleShape)
                    .background(BlueDark.copy(alpha = 0.9f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = name.firstOrNull()?.uppercase() ?: "A",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            // Nombre del Árbitro
            Text(
                text = name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f).padding(horizontal = 10.dp),
                color = BlueDark,
                maxLines = 1 // Para que no se deforme si el nombre es muy largo
            )

            // Lógica inteligente para el Precio
            val textoPrecio = when {
                price == "Pendiente" || price == "Precio pendiente" -> "Pendiente"
                price.contains("$") -> price // Ya tiene signo
                else -> "$$price" // Le agregamos signo
            }

            // Color del precio: Rojo si es dinero, Gris si es Pendiente
            val colorPrecio = if (textoPrecio == "Pendiente") Color.Gray else RedAccent

            Text(
                text = textoPrecio,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = colorPrecio
            )
        }
    }
}