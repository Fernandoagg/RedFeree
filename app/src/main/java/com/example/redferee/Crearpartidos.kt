package com.example.redferee

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch

class Crearpartidos : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                AppNavigationCrearPartido()
            }
        }
    }
}

// --- NAVEGACIÓN LOCAL ---
@Composable
fun AppNavigationCrearPartido() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "lista") {
        composable("lista") { ListaArbitrosScreen(navController) }
        composable("agendar") { AgendarScreen(navController) }
    }
}

// --- ESTILOS ---
val ColorFondoClarito = Color(0xFFF0F4F4)
val ColorBotonOscuro = Color(0xFF4A4A4A)
val ColorVerdeHeader = Color(0xFFE0F2F1)
val ColorAzulAnalytics = Color(0xFF4A90E2)

// ==========================================
// PANTALLA 1: LISTA (MODIFICADA ALTURA)
// ==========================================
@Composable
fun ListaArbitrosScreen(navController: NavController) {
    var listaOriginal by remember { mutableStateOf<List<EquipoArbitro>>(emptyList()) }
    var listaVisible by remember { mutableStateOf<List<EquipoArbitro>>(emptyList()) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        try {
            listaOriginal = RetrofitClient.apiService.obtenerListaEquipos()
            listaVisible = listaOriginal
        } catch (e: Exception) {
            println("Error cargando datos: ${e.message}")
        }
    }

    Scaffold { padding ->
        Box(modifier = Modifier.fillMaxSize().background(ColorFondoClarito).padding(padding)) {
            Column(modifier = Modifier.fillMaxSize()) {
                
                // --- HEADER ---
                // CAMBIO AQUÍ: Reducimos la altura de 260dp a 210dp
                Box(modifier = Modifier.fillMaxWidth().height(210.dp)) {
                    HeaderBackground()
                    
                    // FLECHA DE REGRESO
                    IconButton(
                        onClick = { (context as? Activity)?.finish() },
                        modifier = Modifier
                            .padding(top = 40.dp, start = 16.dp)
                            .align(Alignment.TopStart)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack, 
                            contentDescription = "Volver",
                            tint = ColorBotonOscuro
                        )
                    }

                    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                        Spacer(modifier = Modifier.height(60.dp)) // Espacio para la flecha
                        Text(
                            "Contratación de equipos completos de árbitros",
                            fontSize = 24.sp, // Reduje un poco la fuente también
                            lineHeight = 28.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        
                        // Botones de ordenar
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            FilterButton(
                                text = "Ordenar por: Precio", 
                                modifier = Modifier.weight(1f).clickable {
                                    listaVisible = listaOriginal.sortedBy { 
                                        it.precio.replace(Regex("[^0-9.]"), "").toDoubleOrNull() ?: 0.0 
                                    }
                                }
                            )
                            FilterButton(
                                text = "Ordenar por: Calif.", 
                                modifier = Modifier.weight(1f).clickable {
                                    listaVisible = listaOriginal.sortedByDescending { it.reseñasCount }
                                }
                            )
                        }
                    }
                }

                // LISTA SCROLLABLE
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.weight(1f) 
                ) {
                    items(listaVisible) { equipo ->
                        RefereeCard(equipo, navController)
                    }
                }
            }
        }
    }
}

@Composable
fun RefereeCard(equipo: EquipoArbitro, navController: NavController) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Surface(modifier = Modifier.size(60.dp), shape = CircleShape, color = Color.LightGray) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = equipo.nombre.firstOrNull()?.uppercase() ?: "A",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(equipo.nombre, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("(Lider: ${equipo.lider ?: "N/A"})", fontSize = 12.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(4.dp))
                Text("★★★★☆ (${equipo.reseñasCount} Reseñas)", fontSize = 10.sp)
                Spacer(modifier = Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(
                        onClick = { navController.navigate("agendar") },
                        modifier = Modifier.height(35.dp)
                    ) {
                        Text("Ver Detalles", fontSize = 11.sp, color = Color.DarkGray)
                    }
                    Button(
                        onClick = { navController.navigate("agendar") },
                        colors = ButtonDefaults.buttonColors(containerColor = ColorBotonOscuro),
                        modifier = Modifier.height(35.dp)
                    ) {
                        Text(equipo.precio, fontSize = 11.sp)
                    }
                }
            }
        }
    }
}

// ==========================================
// PANTALLA 2: AGENDAR
// ==========================================
@Composable
fun AgendarScreen(navController: NavController) {
    var listaArbitrosReales by remember { mutableStateOf<List<EquipoArbitro>>(emptyList()) }
    var arbitroSeleccionado by remember { mutableStateOf<EquipoArbitro?>(null) }
    var deporteSeleccionado by remember { mutableStateOf("") }
    var mostrarDialogo by remember { mutableStateOf(false) }
    var estaGuardando by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        try { listaArbitrosReales = RetrofitClient.apiService.obtenerListaEquipos() } catch (e: Exception) {}
    }

    Scaffold(containerColor = ColorFondoClarito) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            HeaderBackground() // Este usará el tamaño por defecto (grande) en esta pantalla, está bien.
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.padding(top = 40.dp, start = 16.dp).align(Alignment.TopStart)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
            }

            Column(
                modifier = Modifier.fillMaxSize().padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(60.dp))
                Text(
                    "Agendar Nuevo Arbitraje",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(40.dp))

                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text("Selecciona el Árbitro", fontWeight = FontWeight.SemiBold, color = Color.Gray)
                        DropdownArbitros(listaArbitrosReales, arbitroSeleccionado) { arbitroSeleccionado = it }
                        Spacer(modifier = Modifier.height(20.dp))
                        Text("Deporte", fontWeight = FontWeight.SemiBold, color = Color.Gray)
                        DropdownGenerico(
                            "Selecciona deporte",
                            listOf("Fútbol 11", "Fútbol 7", "Rápido"),
                            deporteSeleccionado
                        ) { deporteSeleccionado = it }
                        Spacer(modifier = Modifier.height(24.dp))
                        HorizontalDivider()
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                            Text("Total:", fontWeight = FontWeight.Bold)
                            Text(
                                arbitroSeleccionado?.precio ?: "--",
                                fontWeight = FontWeight.Bold,
                                color = ColorBotonOscuro,
                                fontSize = 18.sp
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = {
                        if (arbitroSeleccionado != null && deporteSeleccionado.isNotEmpty()) {
                            estaGuardando = true
                            scope.launch {
                                try {
                                    val nuevoPartido = PartidoRequest(
                                        nombreArbitro = arbitroSeleccionado!!.nombre,
                                        deporte = deporteSeleccionado,
                                        precio = arbitroSeleccionado!!.precio
                                    )
                                    RetrofitClient.apiService.guardarPartido(nuevoPartido)
                                    estaGuardando = false
                                    mostrarDialogo = true
                                } catch (e: Exception) {
                                    estaGuardando = false
                                    Toast.makeText(context, "Error al guardar: ${e.message}", Toast.LENGTH_LONG).show()
                                }
                            }
                        } else {
                            Toast.makeText(context, "Completa los campos", Toast.LENGTH_SHORT).show()
                        }
                    },
                    enabled = !estaGuardando,
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ColorBotonOscuro),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    if (estaGuardando) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    else Text("Confirmar Agenda")
                }
            }
            if (mostrarDialogo) {
                ConfirmacionDialog(arbitroSeleccionado?.nombre ?: "") {
                    mostrarDialogo = false
                    navController.popBackStack()
                }
            }
        }
    }
}

// --- COMPONENTES COMPARTIDOS ---

// CAMBIO IMPORTANTE AQUÍ: Se eliminó la altura fija.
@Composable
fun HeaderBackground() {
    // Ahora llena el espacio que le de el padre
    Box(modifier = Modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(color = ColorVerdeHeader)
            drawCircle(color = Color(0xFFB2DFDB), radius = size.width * 0.6f, center = Offset(0f, 0f))
            drawCircle(
                color = Color(0xFF80CBC4).copy(alpha = 0.5f),
                radius = size.width * 0.35f,
                center = Offset(size.width * 0.1f, size.height * 0.15f)
            )
        }
    }
}

@Composable
fun FilterButton(text: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
            .background(Color.White)
            .padding(horizontal = 4.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text, color = Color.Gray, fontSize = 12.sp)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownArbitros(lista: List<EquipoArbitro>, selec: EquipoArbitro?, onSel: (EquipoArbitro) -> Unit) {
    var exp by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded = exp, onExpandedChange = { exp = !exp }) {
        OutlinedTextField(
            value = selec?.nombre ?: "",
            onValueChange = {},
            readOnly = true,
            placeholder = { Text("Cargando...") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = exp) },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        if (lista.isNotEmpty()) {
            ExposedDropdownMenu(expanded = exp, onDismissRequest = { exp = false }, modifier = Modifier.background(Color.White)) {
                lista.forEach { item ->
                    DropdownMenuItem(text = { Text(item.nombre) }, onClick = { onSel(item); exp = false })
                    HorizontalDivider()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownGenerico(label: String, ops: List<String>, selec: String, onSel: (String) -> Unit) {
    var exp by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded = exp, onExpandedChange = { exp = !exp }) {
        OutlinedTextField(
            value = selec,
            onValueChange = {},
            readOnly = true,
            placeholder = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = exp) },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = exp,
            onDismissRequest = { exp = false },
            modifier = Modifier.background(Color.White)
        ) {
            ops.forEach { op ->
                DropdownMenuItem(
                    text = { Text(op) },
                    onClick = { onSel(op); exp = false },
                    leadingIcon = { Icon(Icons.Default.PlayArrow, null) })
            }
        }
    }
}

@Composable
fun ConfirmacionDialog(nombre: String, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF4CAF50), modifier = Modifier.size(48.dp)) },
        title = { Text("¡Agendado!") },
        text = { Text("Se ha guardado el partido con $nombre en la base de datos.") },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = ColorBotonOscuro)
            ) { Text("Aceptar") }
        },
        containerColor = Color.White
    )
}

@Preview(showBackground = true, name = "1. Pantalla Lista")
@Composable
fun ListaArbitrosScreenPreview() {
    val navController = rememberNavController()
    ListaArbitrosScreen(navController)
}