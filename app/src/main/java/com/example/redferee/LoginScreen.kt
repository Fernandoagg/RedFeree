package com.example.redferee

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.redferee.ui.theme.RedFereeTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavHostController,
                onBack: () -> Unit,
                onLoginSuccess: (LoginResponse) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val repo = remember { UserApiRepositoryLogin() }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isSubmitting by remember { mutableStateOf(false) }
    var showPassword by remember { mutableStateOf(false) }

    // Helpers
    fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        // Fondos circulares — igual estilo que antes
        Box(
            modifier = Modifier
                .size(200.dp)
                .offset(x = 0.dp, y = (-100).dp)
                .clip(CircleShape)
                .background(Color(0xFFB3CDE0).copy(alpha = 0.7f))
        )
        Box(
            modifier = Modifier
                .size(200.dp)
                .offset(x = (-100).dp, y = (0).dp)
                .clip(CircleShape)
                .background(Color(0xFFE6A5B6).copy(alpha = 0.8f))
        )

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { Text("Iniciar Sesión") },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                        }
                    }
                )
            },
            bottomBar = {
                Surface(color = Color.Transparent, modifier = Modifier.padding(16.dp)) {
                    Button(
                        onClick = {
                            // Validación simple: email y password
                            if (email.isBlank() || password.isBlank()) {
                                Toast.makeText(context, "Completa email y contraseña", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            if (!isEmailValid(email)) {
                                Toast.makeText(context, "Email inválido", Toast.LENGTH_SHORT).show()
                                return@Button
                            }

                            // Ejecutar login
                            isSubmitting = true
                            scope.launch {
                                val request = LoginRequest(email = email.trim(), password = password)
                                val result = withContext(Dispatchers.IO) { repo.loginUser(request) }
                                isSubmitting = false

                                if (result.isSuccess) {
                                    val user = result.getOrThrow()
                                    Toast.makeText(context, "Bienvenido ${user.nombre}", Toast.LENGTH_SHORT).show()
                                    onLoginSuccess(user) // navega / guarda token / etc
                                } else {
                                    val err = result.exceptionOrNull()?.message ?: "Error desconocido"
                                    Toast.makeText(context, "Error: $err", Toast.LENGTH_LONG).show()
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4F5D75)),
                        enabled = !isSubmitting
                    ) {
                        if (isSubmitting) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                        } else {
                            Text("Iniciar Sesión", fontSize = 18.sp, fontWeight = FontWeight.Bold)
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
                Spacer(modifier = Modifier.height(12.dp))

                Text("Correo Electrónico", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("ejemplo@dominio.com") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White.copy(alpha = 0.9f),
                        unfocusedContainerColor = Color.White.copy(alpha = 0.9f)
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(autoCorrect = false)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text("Contraseña", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { showPassword = !showPassword }) {
                            Icon(
                                imageVector = if (showPassword) Icons.Default.Visibility else Icons.Default.Visibility,
                                contentDescription = "Mostrar/Ocultar"
                            )
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White.copy(alpha = 0.9f),
                        unfocusedContainerColor = Color.White.copy(alpha = 0.9f)
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Opcional: link a "Crear cuenta"
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    Text(
                        text = "¿No tienes cuenta? Regístrate",
                        modifier = Modifier
                            .clickable {
                                navController.navigate("register")
                            }
                            .padding(8.dp)
                        ,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
