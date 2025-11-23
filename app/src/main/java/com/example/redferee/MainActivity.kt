package com.example.redferee

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

// --- ACTIVITY PRINCIPAL ---
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RedFereeTheme {
                // Configuración de la navegación
                val navController: NavHostController = rememberNavController()
                NavHost(navController = navController, startDestination = "main") {

                    // Ruta 1: Home (MainScreen)
                    composable("main") {
                        MainScreen(navController)
                    }

                    // Ruta 2: Reseñas (ReviewsScreen desde CalisRese.kt)
                    composable("reviews") {
                        // Llamamos a la función que está en CalisRese.kt
                        // Pasamos una lambda para que al dar 'Atrás', el navController saque esta pantalla de la pila
                        ReviewsScreen(onBack = { navController.popBackStack() })
                    }
                }
            }
        }
    }
}

// --- PANTALLA PRINCIPAL (HOME) ---

@Composable
fun MainScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TopBar()

        Spacer(modifier = Modifier.height(16.dp))

        SearchBar()

        Spacer(modifier = Modifier.height(24.dp))

        SectionTitle("Acceso Rápido")

        QuickAccessCards()

        Spacer(modifier = Modifier.height(24.dp))

        SectionTitle("Árbitros mejor calificados cerca de ti")

        // Al hacer clic en esta tarjeta, navegamos a la ruta "reviews"
        HighlightedRefereesCard { navController.navigate("reviews") }

        Spacer(modifier = Modifier.height(24.dp))

        SectionTitle("Recursos y Tips")

        ResourcesTipsCard()

        Spacer(modifier = Modifier.weight(1f))

        BottomNavigationBar()
    }
}

// --- COMPONENTES DE LA PANTALLA PRINCIPAL ---

@Composable
fun TopBar() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "RedFeree",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Row {
            Icon(imageVector = Icons.Default.Person, contentDescription = "Perfil")
            Spacer(modifier = Modifier.width(12.dp))
            Icon(imageVector = Icons.Default.Notifications, contentDescription = "Notificaciones")
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
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
fun QuickAccessCards() {
    Row(modifier = Modifier.fillMaxWidth()) {

        Card(
            modifier = Modifier
                .weight(1f)
                .height(150.dp)
                .padding(4.dp)
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Próximo partido", fontWeight = FontWeight.Medium)
                Text("Fútbol - Sábado, 2pm", fontWeight = FontWeight.Bold)
            }
        }

        Card(
            modifier = Modifier
                .weight(1f)
                .height(150.dp)
                .padding(4.dp)
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
            .clickable(onClick = onClick) // Hacemos la tarjeta clicable
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text("Árbitros destacados (Ver Reseñas)", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun ResourcesTipsCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF0F0F0))
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Text("Inicio")
        Text("Búsqueda")
        Text("Contratar")
        Text("Mis Partidos")
        Text("Más")
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