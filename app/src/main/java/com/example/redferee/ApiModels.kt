package com.example.redferee

import com.google.gson.annotations.SerializedName

// ==========================================
// 1. MODELOS DE HISTORIAL (F3)
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
// 2. MODELOS DE RESEÑAS (F4)
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
    val id_insertado: Int? = null
)

data class ApiResponse(
    val status: String,
    val count: Int,
    val reviews: List<Review>
)

// ==========================================
// 3. MODELOS DE CREAR PARTIDO (F2 - NUEVO)
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

// Respuesta al agendar (Backend devuelve message e id)
data class PartidoResponse(
    val message: String,
    val id: Int
)