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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.redferee.ui.theme.RedFereeTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Repository
    val repository = remember { UserApiRepository() }

    // Estados del formulario
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isSubmitting by remember { mutableStateOf(false) }

    // FONDO decorativo
    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {

        // 1. Círculo Azul
        Box(
            modifier = Modifier
                .size(200.dp)
                .offset(x = 0.dp, y = (-100).dp)
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

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { Text("Crear Cuenta") },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
                        }
                    }
                )
            },
            bottomBar = {
                Button(
                    onClick = {
                        if (nombre.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                            isSubmitting = true

                            scope.launch {
                                val request = RegisterRequest(
                                    nombre = nombre,
                                    email = email,
                                    password = password
                                )

                                val result = withContext(Dispatchers.IO) {
                                    repository.registerUser(request)
                                }

                                isSubmitting = false

                                if (result.isSuccess) {
                                    Toast.makeText(context, "¡Usuario registrado!", Toast.LENGTH_LONG).show()
                                    onBack()
                                } else {
                                    val msg = result.exceptionOrNull()?.message ?: "Error desconocido"
                                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                                }
                            }
                        } else {
                            Toast.makeText(context, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4F5D75)),
                    enabled = !isSubmitting
                ) {
                    if (isSubmitting) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Text("Crear Cuenta", fontSize = 18.sp, fontWeight = FontWeight.Bold)
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

                // Campo Nombre
                Text("Nombre", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre completo") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White.copy(alpha = 0.9f),
                        unfocusedContainerColor = Color.White.copy(alpha = 0.9f)
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Campo Email
                Text("Correo Electrónico", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Correo") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White.copy(alpha = 0.9f),
                        unfocusedContainerColor = Color.White.copy(alpha = 0.9f)
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Campo Password
                Text("Contraseña", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White.copy(alpha = 0.9f),
                        unfocusedContainerColor = Color.White.copy(alpha = 0.9f)
                    )
                )
            }
        }
    }
}
