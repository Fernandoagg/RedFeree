package com.example.redferee

import com.google.gson.annotations.SerializedName

// ==========================================
// 1. MODELOS DE AUTENTICACIÓN (LOGIN/REGISTER - F1)
// ==========================================
data class LoginRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

data class LoginResponse(
    val nombre: String,
    val email: String,
    val password: String,
    val tipo: String // o los campos que devuelva tu backend al hacer login
)

data class RegisterRequest(
    @SerializedName("nombre") val nombre: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("tipo") val tipo: String = "usuario"
)

data class RegisterResponse(
    val status: String,
    val message: String,
    @SerializedName("id_insertado") val userId: Int? = null
)

// ==========================================
// 2. MODELOS DE HISTORIAL (F3)
// ==========================================
data class PartidoBackend(
    val id: Int,
    val fecha: String,
    val ubicacion: String,
    val deporte: String,
    val nombreArbitro: String?,
    val costo: String?
)

// ==========================================
// 3. MODELOS DE CREAR PARTIDO (F2)
// ==========================================
data class EquipoArbitro(
    val id: Int,
    val nombre: String,
    val lider: String?,
    val reseñasCount: Int = 0,
    val precio: String
)

data class PartidoRequest(
    val nombreArbitro: String,
    val deporte: String,
    val precio: String
)

data class PartidoResponse(
    val message: String,
    val id: Int
)

// ==========================================
// 4. MODELOS DE RESEÑAS Y RESPUESTA GENERAL (F4)
// ==========================================
data class Review(
    val NombreArbi: String,
    val Cali: Int,
    val userName: String,
    val Resena: String,
    val tiempo: String
)

data class Referee(
    @SerializedName("id") val id: Int,
    @SerializedName("nombre") val nombre: String
)

data class ReviewRequest(
    @SerializedName("arbitroid") val arbitroid: Int,
    @SerializedName("usuarioid") val usuarioid: Int,
    @SerializedName("estrellas") val estrellas: Int,
    @SerializedName("texto") val texto: String
)

data class PostResponse(
    val status: String,
    val message: String,
    @SerializedName("id_insertado") val idInsertado: Int? = null
)

data class ApiResponse(
    val status: String,
    val count: Int,
    val reviews: List<Review>
)