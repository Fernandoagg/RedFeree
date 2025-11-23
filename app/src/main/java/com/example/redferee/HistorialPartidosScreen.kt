package com.example.redferee

import android.util.Log // Importante para ver errores en Logcat
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
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
import androidx.lifecycle.viewModelScope // Importante para las corrutinas
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch // Importante para lanzar la petición asíncrona
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

// ---------------------------------------------------------
// 1. MODELO DE DATOS Y VIEWMODEL (LÓGICA CONECTADA A NODE.JS)
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
                // 1. LLAMADA AL SERVIDOR (Node.js)
                val respuestaBackend = RetrofitClient.apiService.obtenerPartidos()

                // 2. MAPEO: Convertimos los datos JSON a tu modelo visual
                listaPartidos = respuestaBackend.map { item ->
                    Partido(
                        id = item.id,
                        // Si el backend no manda nombre (null), ponemos uno genérico
                        nombreArbitro = item.nombreArbitro ?: "Árbitro #${item.id}",
                        // Si el backend no manda costo (null), ponemos "Pendiente"
                        costo = item.costo ?: "Pendiente"
                    )
                }
            } catch (e: Exception) {
                // Si falla, lo imprimimos en la consola de "Logcat" abajo
                Log.e("API_ERROR", "Error conectando a Node: ${e.message}")

                // Opcional: Mostrar un error visual en la lista para saber qué pasó
                listaPartidos = listOf(
                    Partido(0, "Error de Conexión", "Check Server")
                )
            }
        }
    }
}

// ---------------------------------------------------------
// 2. CONSTANTES DE DISEÑO (TUS COLORES)
// ---------------------------------------------------------
private val BlueDark = Color(0xFF0C2847)
private val RedAccent = Color(0xFFD33A35)
private val LightGrayBg = Color(0xFFE8ECEF)

// ---------------------------------------------------------
// 3. PANTALLA PRINCIPAL (VISUAL IGUAL QUE ANTES)
// ---------------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistorialPartidosScreen(
    onBack: () -> Unit,
    // Inyectamos el ViewModel aquí automáticamente
    viewModel: HistorialViewModel = viewModel()
) {
    // Obtenemos la lista viva del ViewModel (que ahora viene de Internet)
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

        // Contenido principal con Fondo y Decoración
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(LightGrayBg)
        ) {
            // --- DECORACIÓN DE FONDO ---
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .offset(x = (-40).dp, y = (-40).dp)
                    .clip(CircleShape)
                    .background(Color(0x330C2847))
            )

            Box(
                modifier = Modifier
                    .size(180.dp)
                    .offset(x = (250).dp, y = (-60).dp)
                    .clip(CircleShape)
                    .background(Color(0x33D33A35))
            )

            // --- LISTA DINÁMICA ---
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(top = 20.dp, bottom = 20.dp)
            ) {

                // SECCIÓN A: Espaciador inicial
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }

                // SECCIÓN B: La lista de árbitros (Viene de Node.js)
                items(partidos) { partido ->
                    RefereeCard(name = partido.nombreArbitro, price = partido.costo)
                    Spacer(modifier = Modifier.height(18.dp))
                }

                // SECCIÓN C: El Footer (Logo y Paginación)
                item {
                    Spacer(modifier = Modifier.height(20.dp))

                    Image(
                        painter = painterResource(id = R.drawable.redfere),
                        contentDescription = "Logo",
                        modifier = Modifier
                            .height(150.dp)
                            .padding(10.dp),
                        contentScale = ContentScale.Fit
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
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
// 4. TU TARJETA PERSONALIZADA (ESTILO)
// ---------------------------------------------------------
@Composable
fun RefereeCard(name: String, price: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(2.dp, BlueDark),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(45.dp)
                    .clip(CircleShape)
                    .background(BlueDark.copy(alpha = 0.9f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "A",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
                    .padding(horizontal = 10.dp),
                color = BlueDark
            )

            Text(
                text = if(price.contains("$")) price else "$price$", // Pequeño ajuste por si no trae el signo
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = RedAccent
            )
        }
    }
}