package com.example.reedfere


// --- PEGA ESTO DEBAJO DE LA LÍNEA 1 (package...) ---

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                ArbitrosScreenApp()
            }
        }
    }
}

// --- 1. MODELO DE DATOS (Debe coincidir con tu JSON de Thunder Client) ---
data class EquipoArbitro(
    val id: Int,
    val nombre: String,
    val lider: String,
    val reseñasCount: Int,
    val rating: Double, // Agregamos rating que viene de tu BD
    val precio: String
)

// --- 2. CONFIGURACIÓN DE RETROFIT (LA CONEXIÓN) ---
// Definimos la "firma" de tu API
interface ApiService {
    @GET("api/lista-arbitros") // La ruta exacta de tu backend
    suspend fun obtenerArbitros(): List<EquipoArbitro>
}

// Creamos el objeto que se conecta
object RetrofitClient {
    // OJO: 10.0.2.2 es "localhost" para el emulador de Android
    private const val BASE_URL = "http://10.0.2.2:3000/"

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}

val ColorFondoClarito = Color(0xFFF0F4F4)
val ColorBotonOscuro = Color(0xFF4A4A4A)
val ColorAzulAnalytics = Color(0xFF4A90E2)

// --- PANTALLA PRINCIPAL ---
@Composable
fun ArbitrosScreenApp() {
    // ESTADO: Aquí guardamos la lista que viene de internet
    // Empieza vacía y se llena cuando responde el servidor
    var listaDeEquipos by remember { mutableStateOf<List<EquipoArbitro>>(emptyList()) }

    // EFECTO: Esto se ejecuta una sola vez al abrir la app
    LaunchedEffect(Unit) {
        try {
            // Llamamos a tu backend
            val respuesta = RetrofitClient.api.obtenerArbitros()
            listaDeEquipos = respuesta
        } catch (e: Exception) {
            // Si falla (ej. servidor apagado), imprime el error en la consola (Logcat)
            Log.e("API_ERROR", "Error al conectar: ${e.message}")
        }
    }

    Scaffold(
        bottomBar = { BottomNavigationBar() }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorFondoClarito)
                .padding(paddingValues)
        ) {
            // Le pasamos la lista REAL a la pantalla
            MainContent(listaDeEquipos)
        }
    }
}

@Composable
fun MainContent(arbitros: List<EquipoArbitro>) {
    Column(modifier = Modifier.fillMaxSize()) {

        // --- ENCABEZADO ---
        Box(modifier = Modifier.fillMaxWidth().height(260.dp)) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawRect(color = Color(0xFFE0F2F1))
                drawCircle(color = Color(0xFFB2DFDB), radius = size.width * 0.55f, center = Offset(0f, 0f))
                drawCircle(color = Color(0xFF80CBC4).copy(alpha = 0.5f), radius = size.width * 0.3f, center = Offset(size.width * 0.1f, size.height * 0.1f))
            }
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "Contratación de equipos completos de árbitros",
                    fontSize = 28.sp, lineHeight = 34.sp, fontWeight = FontWeight.Medium, color = Color.Black,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    FilterButton(text = "Ordenar por: Precio", modifier = Modifier.weight(1f))
                    FilterButton(text = "Ordenar por: Calificacion", modifier = Modifier.weight(1f))
                }
            }
        }

        // --- LISTA DE TARJETAS ---
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // AQUI LA MAGIA: Usamos la lista que vino de internet
            items(arbitros) { equipo ->
                RefereeCard(equipo = equipo)
            }
        }
    }
}

// ... (El resto de componentes FilterButton, RefereeCard y BottomBar siguen igual) ...
// Solo pego RefereeCard y FilterButton para asegurarnos que no falte nada

@Composable
fun FilterButton(text: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
            .background(Color.White)
            .padding(horizontal = 4.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, color = Color.Gray, fontSize = 12.sp)
    }
}

@Composable
fun RefereeCard(equipo: EquipoArbitro) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Surface(modifier = Modifier.size(60.dp), shape = CircleShape, color = Color.LightGray) {}
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = equipo.nombre, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(text = "(Lider: ${equipo.lider})", fontSize = 12.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    repeat(4) { Icon(Icons.Filled.Star, null, modifier = Modifier.size(14.dp), tint = Color.Black) }
                    Icon(Icons.Outlined.Star, null, modifier = Modifier.size(14.dp), tint = Color.Black)
                    Text(text = " (${equipo.reseñasCount} Reseñas)", fontSize = 10.sp, color = Color.Gray)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Composicion: 1 Arbitro central, 2 Asistentes", fontSize = 10.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    OutlinedButton(onClick = { }, shape = RoundedCornerShape(4.dp), contentPadding = PaddingValues(0.dp), modifier = Modifier.height(35.dp).width(100.dp)) {
                        Text("Ver Detalles", fontSize = 11.sp, color = Color.DarkGray)
                    }
                    Button(onClick = { }, colors = ButtonDefaults.buttonColors(containerColor = ColorBotonOscuro), shape = RoundedCornerShape(4.dp), contentPadding = PaddingValues(0.dp), modifier = Modifier.height(35.dp).width(100.dp)) {
                        Text(equipo.precio, fontSize = 11.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar() {
    NavigationBar(containerColor = Color.White, contentColor = Color.Gray) {
        NavigationBarItem(icon = { Icon(Icons.Default.Home, "Inicio") }, selected = false, onClick = { })
        NavigationBarItem(icon = { Icon(Icons.Default.Search, "Buscar") }, selected = false, onClick = { })
        NavigationBarItem(icon = { Icon(Icons.Default.List, "Analytics") }, selected = true, onClick = { }, colors = NavigationBarItemDefaults.colors(selectedIconColor = ColorAzulAnalytics, indicatorColor = Color.White))
        NavigationBarItem(icon = { Icon(Icons.Default.DateRange, "Historial") }, selected = false, onClick = { })
        NavigationBarItem(icon = { Icon(Icons.Default.Person, "Perfil") }, selected = false, onClick = { })
    }
}