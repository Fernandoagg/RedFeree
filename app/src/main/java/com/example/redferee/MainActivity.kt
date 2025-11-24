package com.example.redferee

import android.content.Intent // Importante para navegar
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext // Importante para el contexto
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.redferee.ui.theme.RedFereeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Activa el diseño de borde a borde
        setContent {
            RedFereeTheme {
                // Creamos el controlador de navegación
                val navController = rememberNavController()
                // Iniciamos la navegación
                AppNavigation(navController)
            }
        }
    }
}

// --- CONFIGURACIÓN DE NAVEGACIÓN ---

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "main") {

        // RUTA 1: PANTALLA PRINCIPAL (HOME)
        composable("main") {
            MainScreen(navController)
        }

        // RUTA 2: PANTALLA DE RESEÑAS (LISTA)
        composable("reviews") {
            // Llamamos a ReviewsScreen (que está en CalisRese.kt)
            ReviewsScreen(
                onBack = { navController.popBackStack() },
                onWriteReview = { navController.navigate("write_review") }
            )
        }

        // RUTA 3: PANTALLA DE ESCRIBIR RESEÑA (FORMULARIO)
        composable("write_review") {
            // Llamamos a WriteReviewScreen (que está en WriteReviewScreen.kt)
            WriteReviewScreen(
                onBack = { navController.popBackStack() }
            )
        }
        composable("register") {
            // Llamamos a WriteReviewScreen (que está en WriteReviewScreen.kt)
            RegisterScreen(
                onBack = { navController.popBackStack() }
            )

        }
        composable("login") {
            LoginScreen(
                navController = navController,
                onBack = { navController.popBackStack() },
                onLoginSuccess = { navController.navigate("main")}
            )
        }



    }
}

// --- PANTALLA PRINCIPAL (UI) ---

@Composable
fun MainScreen(navController: NavController) {
    // CONTENEDOR PRINCIPAL CON FONDO DECORATIVO
    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {

        // --- CÍRCULOS DECORATIVOS ---

        // 1. Círculo Azul (Atrás)
        Box(
            modifier = Modifier
                .size(200.dp)
                .offset(x = (0).dp, y = (-100).dp)
                .clip(CircleShape)
                .background(Color(0xFFB3CDE0).copy(alpha = 0.7f))
        )

        // 2. Círculo Rosa (Adelante)
        Box(
            modifier = Modifier
                .size(200.dp)
                .offset(x = (-100).dp, y = (0).dp)
                .clip(CircleShape)
                .background(Color(0xFFE6A5B6).copy(alpha = 0.8f))
        )

        // --- CONTENIDO DE LA PANTALLA ---
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(35.dp)
                .verticalScroll(rememberScrollState())
        ) {
            TopBar({navController.navigate("login")})

            Spacer(modifier = Modifier.height(16.dp))

            SearchBar()

            Spacer(modifier = Modifier.height(24.dp))

            SectionTitle("Acceso Rápido")

            QuickAccessCards()

            Spacer(modifier = Modifier.height(24.dp))

            SectionTitle("Árbitros calificados")

            // Al hacer clic, navegamos a la lista de reseñas ("reviews")
            HighlightedRefereesCard { navController.navigate("reviews") }

            Spacer(modifier = Modifier.height(24.dp))

            SectionTitle("Recursos y Tips")

            ResourcesTipsCard()

            Spacer(modifier = Modifier.height(40.dp)) // Espacio final

            BottomNavigationBar()
        }
    }
}

// --- COMPONENTES VISUALES ---

@Composable
fun TopBar(onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "RedFeree",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )


        Card(
            modifier = Modifier
                .clickable(onClick = onClick),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE8EAF6)) // Un azul muy suave
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.width(100.dp)
            ) {
                Row {
                    Icon(imageVector = Icons.Default.Person, contentDescription = "Perfil", tint = Color.Black)
                    Spacer(modifier = Modifier.width(12.dp))
                    Icon(imageVector = Icons.Default.Notifications, contentDescription = "Notificaciones", tint = Color.Black)
                }
            }
        }

    }
}

@Composable
fun SearchBar() {
    OutlinedTextField(
        value = "",
        onValueChange = {},
        placeholder = { Text("¿Necesitas un árbitro?") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White.copy(alpha = 0.8f),
            unfocusedContainerColor = Color.White.copy(alpha = 0.8f)
        )
    )
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(vertical = 8.dp),
        color = Color.Black
    )
}

@Composable
fun QuickAccessCards() {
    // Obtenemos el contexto para poder lanzar la nueva Activity
    val context = LocalContext.current

    Row(modifier = Modifier.fillMaxWidth()) {

        // --- TARJETA 1: CREAR PARTIDO (AHORA CON CLICK) ---
        Card(
            modifier = Modifier
                .weight(1f)
                .height(150.dp)
                .padding(4.dp)
                .clickable {
                    // Aquí lanzamos la actividad de tu compañero
                    val intent = Intent(context, Crearpartidos::class.java)
                    context.startActivity(intent)
                },
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Próximo partido", fontWeight = FontWeight.Medium)
                Text("Fútbol - Sábado, 2pm", fontWeight = FontWeight.Bold)
            }
        }

        // --- TARJETA 2: HISTORIAL ---
        Card(
            modifier = Modifier
                .weight(1f)
                .height(150.dp)
                .padding(4.dp)
                .clickable {
                    val intent = Intent(context, HistorialPartidosActivity::class.java)
                    context.startActivity(intent)
                },
            // Mantenemos el color de fondo
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Historial", fontWeight = FontWeight.Medium)
                Text("Ver todos tus partidos", fontWeight = FontWeight.Bold)
            }
        }
    }
}


@Composable
fun HighlightedRefereesCard(onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8EAF6))
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text("Ver Reseñas de Nuestros Árbitros", fontWeight = FontWeight.Bold, color = Color(0xFF3F51B5))
        }
    }
}

@Composable
fun ResourcesTipsCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text("Consejos de arbitraje", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text("Descubre más", fontSize = 14.sp)
        }
    }
}

@Composable
fun BottomNavigationBar() {
    Surface(
        shadowElevation = 8.dp,
        color = Color(0xFFF0F0F0)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text("Inicio", fontWeight = FontWeight.Bold)
            Text("Búsqueda")
            Text("Contratar")
            Text("Mis Partidos")
            Text("Más")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainScreenPreview() {
    RedFereeTheme {
        val navController = rememberNavController()
        MainScreen(navController)
    }
}